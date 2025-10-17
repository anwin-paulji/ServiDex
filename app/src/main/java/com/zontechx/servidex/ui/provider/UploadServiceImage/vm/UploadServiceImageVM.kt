package com.zontechx.servidex.ui.provider.UploadServiceImage.vm

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.Screen
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.UiEvent
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import com.zontechx.servidex.utils.ImageUtils
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

class UploadServiceImageVM : ViewModel() {
    val TAG = "UploadServiceImageVM"
    private val apiClient = ApiClient.getInstance();
    val pagerIndex = MutableStateFlow(0)
    val uploadTrigger = MutableStateFlow(false)
    var serviceId = MutableStateFlow("")

    val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _selectedImageList = MutableStateFlow<List<ImageData>>(emptyList())
    val selectedImageList = _selectedImageList.asStateFlow()

    private val _uploadImageListResponse =
        MutableStateFlow<ApiResponse<ResponseWrapper<JsonObject>>>(ApiResponse.Initial)
    val uploadImageListResponse = _uploadImageListResponse.asStateFlow()

    fun popTo(route:String = Screen.MainScreen.route) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.PopUpTo(route))
        }
    }

    fun setServiceId(serviceId: String) {
        Log.e(TAG, "setServiceId: $serviceId")
        this.serviceId.value = serviceId
    }

    fun addImageList(imageList: List<Uri>, bitmapList: List<Bitmap>) {
        val current = _selectedImageList.value.toMutableList()

        // Pair uris with bitmaps
        val imageDataList = imageList.zip(bitmapList) { uri, bitmap ->
            ImageData(uri, bitmap)
        }

        // Add only if uri not already in list
        imageDataList.forEach { newImage ->
            if (current.none { it.originalImageUri == newImage.originalImageUri }) {
                if(current.size < 6) {
                    current.add(newImage)
                }
            }
        }

        _selectedImageList.value = current
    }

    fun removeImage(imageData: ImageData) {
        val currentList = _selectedImageList.value.toMutableList()
        currentList.remove(imageData)
        _selectedImageList.value = currentList
    }

    fun switchToImage(clickedIndex: Int) {
        pagerIndex.value = clickedIndex
    }

    fun setCoverImage(index: Int) {
        val updatedList = _selectedImageList.value.mapIndexed { i, imageData ->
            imageData.copy(isCoverImage = i == index)
        }
        _selectedImageList.value = updatedList
    }

    fun uploadImageTrigger() {
        uploadTrigger.value = true
    }

    fun setCroppedImage(croppedImages: List<Bitmap>) {
        val updatedList = _selectedImageList.value.mapIndexed { index, imageData ->
            imageData.copy(croppedImageBitmap = croppedImages[index])
        }
        _selectedImageList.value = updatedList
        uploadServiceImages()
    }

    fun uploadServiceImages() {
        uploadTrigger.value = false
        _uploadImageListResponse.value = ApiResponse.Loading
        val originalImages = selectedImageList.value.map { it.bitmapImage }
        val croppedImages: List<Bitmap> =
            selectedImageList.value.map { it.croppedImageBitmap as Bitmap }

        val finalOriginalImages = ImageUtils.bitmapsToJpegByteArrays(originalImages)
        val finalCroppedImages = ImageUtils.bitmapsToJpegByteArrays(croppedImages)

        viewModelScope.launch {
            try {
                val response: ResponseWrapper<JsonObject> =
                    apiClient.client.submitFormWithBinaryData(
                        url = AppConfig.BASE_URL + ApiEndpoint.UPLOAD_IMAGE,
                        formData = formData {
                            append("serviceId", serviceId.value)

                            // Attach original images
                            finalOriginalImages.forEachIndexed { index, file ->
                                val randomName =
                                    "original_${index}_${System.currentTimeMillis()}_${(1000..9999).random()}.jpg"

                                append(
                                    key = "originalImages[]",
                                    value = file,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentType, "image/jpeg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=$randomName"
                                        )
                                    }
                                )

                                append("isPrimary[]", selectedImageList.value[index].isCoverImage.toString())
                            }

                            // Attach cropped images
                            finalCroppedImages.forEachIndexed { index, file ->
                                val randomName =
                                    "cropped_${index}_${System.currentTimeMillis()}_${(1000..9999).random()}.jpg"

                                append(
                                    key = "croppedImages[]",
                                    value = file,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentType, "image/jpeg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=$randomName"
                                        )
                                    }
                                )
                            }
                        }
                    ) {
                        headers.remove(HttpHeaders.ContentType)
                    }.body()

                if (response.status) {
                    Log.e(TAG, "uploadServiceImages SUCCESS: ${response.message}")
                    _uploadImageListResponse.value = ApiResponse.Success(response)
                } else {
                    Log.e(TAG, "uploadServiceImages FAILED: ${response.message}")
                    _uploadImageListResponse.value = ApiResponse.Error(response.message)
                }

            } catch (e: Exception) {
                Log.e(TAG, "uploadServiceImages: ", e)
                _uploadImageListResponse.value = ApiResponse.Error(e.message.toString())
            }
        }
    }
}

data class ImageData(
    val originalImageUri: Uri,
    val bitmapImage: Bitmap,
    val croppedImageBitmap: Bitmap? = null,
    var isCoverImage: Boolean = false
)