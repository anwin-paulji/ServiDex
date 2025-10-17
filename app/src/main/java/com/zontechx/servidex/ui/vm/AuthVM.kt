package com.zontechx.servidex.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.LoginRegisterResponse
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthVM : ViewModel() {

    private val TAG = "AuthVM"
    private val apiClient = ApiClient.getInstance()

    private var _loginResponse =  MutableSharedFlow<ApiResponse<ResponseWrapper<LoginRegisterResponse>>>()
    var loginResponse: SharedFlow<ApiResponse<ResponseWrapper<LoginRegisterResponse>>> = _loginResponse.asSharedFlow();


    fun checkMobileNumberExist(mobileNumber: String) {
        viewModelScope.launch {
            try {
                _loginResponse.emit(ApiResponse.Loading)

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.LOGIN_REGISTER) {
                        setBody(mapOf("mobile_number" to mobileNumber))
                    }

                Log.e(TAG, "result: $response")

                val result: ResponseWrapper<LoginRegisterResponse> = response.body()

                if (result.status) {
                    _loginResponse.emit(ApiResponse.Success(result))
                } else {
                    _loginResponse.emit(ApiResponse.Error(result.message))
                }
            } catch (e: Exception) {
                _loginResponse.emit(ApiResponse.Error("Exception : ${e.message.toString()}"))
            }
        }
    }
}