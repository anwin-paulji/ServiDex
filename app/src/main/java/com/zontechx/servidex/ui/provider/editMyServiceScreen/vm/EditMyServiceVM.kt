package com.zontechx.servidex.ui.provider.editMyServiceScreen.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.response.UserData
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.ServiceData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EditMyServiceVM : ViewModel() {

    private val TAG = "EditMyServiceVM"
    private val apiClient = ApiClient.getInstance()

    /*********************************************************************************************/
    var selectedEditOption = MutableStateFlow<EditOption?>(null)
    var editOptionTitle = MutableStateFlow<String>("")
    var editOptionValue = MutableStateFlow<String>("")
    var editOptionDescription = MutableStateFlow<String>("")
    var selectedMyServiceData = MutableStateFlow<ServiceData?>(null)

    fun setSelectedServiceData(serviceData: ServiceData) {
        selectedMyServiceData.value = serviceData
    }

    fun onChangeEditValue(value: String) {
        editOptionValue.value = value
    }

    val _updateServiceResponse = MutableSharedFlow<ApiResponse<ServiceData>>()
    val updateServiceResponse = _updateServiceResponse.asSharedFlow()

    fun onChangeEditOption(option: EditOption) {
        selectedEditOption.value = option
        when (option) {
            EditOption.SERVICE_TITLE -> {
                editOptionTitle.value = AppConstant.SERVICE_TITLE
                editOptionDescription.value = AppConstant.SERVICE_TITLE_DESCRIPTION
                editOptionValue.value = selectedMyServiceData.value!!.serviceTitle
            }

            EditOption.SERVICE_DESCRIPTION -> {
                editOptionTitle.value = AppConstant.SERVICE_DESCRIPTION
                editOptionDescription.value = AppConstant.SERVICE_DESCRIPTION_DESCRIPTION
                editOptionValue.value = selectedMyServiceData.value!!.serviceDescription
            }

            else -> {}
        }
    }

    fun onSaveEditOption() {

        viewModelScope.launch {
            try {
                val serviceId = selectedMyServiceData.value!!._id
                val requestBody = buildJsonObject {
                    put("serviceId", JsonPrimitive(serviceId))
                    when (selectedEditOption.value) {
                        EditOption.SERVICE_TITLE -> {
                            put("serviceTitle", JsonPrimitive(editOptionValue.value))
                        }

                        EditOption.SERVICE_DESCRIPTION -> {
                            put("serviceDescription", JsonPrimitive(editOptionValue.value))
                        }

                        else -> {}
                    }
                }

                _updateServiceResponse.emit(ApiResponse.Loading)

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.UPDATE_SERVICE_DATA) {
                        setBody(requestBody)
                    }

                val result: ResponseWrapper<ServiceData> = response.body()

                if (result.status) {

                    if (result.data == null) {
                        _updateServiceResponse.emit(ApiResponse.Error("No Data Found"))
                        return@launch
                    }
                    _updateServiceResponse.emit(ApiResponse.Success(result.data))
                    setSelectedServiceData(result.data)
                } else {
                    _updateServiceResponse.emit(ApiResponse.Error(result.message))
                }

            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
                _updateServiceResponse.emit(ApiResponse.Error(exception.message.toString()))
            }
        }
    }
}

enum class EditOption {
    SERVICE_TITLE,
    SERVICE_DESCRIPTION,
    SERVICE_PRICE,
    SERVICE_CATEGORY,
    SERVICE_IMAGE,
    SERVICE_LOCATION,
    SERVICE_AVAILABILITY,
}