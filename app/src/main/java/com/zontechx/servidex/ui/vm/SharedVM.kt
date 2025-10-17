package com.zontechx.servidex.ui.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.response.UserData
import com.zontechx.servidex.model.LocationData
import com.zontechx.servidex.model.response.ServiceData
import com.zontechx.servidex.model.sealed.AddressData
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SharedVM : ViewModel() {
    val TAG = "SharedVM"
    private val apiClient = ApiClient.getInstance();
    /******************************************************************************
     * * ️⬇️ To Update User Profile ⬇️
     ******************************************************************************/
    var fullName = MutableStateFlow<String>("")
    var showProfileComeletionSheet = MutableStateFlow<Boolean>(false)
    private val _updatedUserProfileResponse = MutableSharedFlow<ApiResponse<UserData>>()
    val updatedUserProfileResponse: SharedFlow<ApiResponse<UserData>> = _updatedUserProfileResponse.asSharedFlow()

    fun onFullNameChange(newFullName: String) {
        fullName.value = newFullName
    }

    fun showProfileCompletionBottomSheet(show: Boolean) {
        fullName.value = ""
        showProfileComeletionSheet.value = show
    }

    fun updateUserProfile() {
        viewModelScope.launch {
            try {

                _updatedUserProfileResponse.emit(ApiResponse.Loading)

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.UPDATE_USER_PROFILE) {
                        setBody(mapOf("userName" to fullName.value))
                    }

                val result: ResponseWrapper<UserData> = response.body()

                if (result.status) {

                    if (result.data == null) {
                        _updatedUserProfileResponse.emit(ApiResponse.Error("No Data Found"))
                        return@launch
                    }
                    setUserData(result.data)
                    _updatedUserProfileResponse.emit(ApiResponse.Success(result.data))
                } else {
                    _updatedUserProfileResponse.emit(ApiResponse.Error(result.message))
                }

            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
                _updatedUserProfileResponse.emit(ApiResponse.Error(exception.message.toString()))
            }
        }
    }
    /******************************************************************************
     * * * ️⬇️ User Data ⬇️ * * *
     ******************************************************************************/
    var userData = MutableStateFlow<UserData?>(null)

    fun setUserData(data: UserData) {
        userData.value = data
    }
    /******************************************************************************
     * * * ️⬇️ Edit Service Details Screen ⬇️ * * *
     ******************************************************************************/
    var selectedMyServiceData = MutableStateFlow<ServiceData?>(null)

    fun setSelectedServiceData(serviceData: ServiceData) {
        selectedMyServiceData.value = serviceData
    }
    /******************************************************************************/
    var currentAddress = MutableStateFlow<LocationData?>(null)
    fun setCurrentAddress(address: LocationData) {
        currentAddress.value = address
    }
    /******************************************************************************/
}