package com.zontechx.servidex.ui.vm

import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.response.VerifyOtpResponse
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import com.zontechx.servidex.utils.FirebaseUtils
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpVerificationScreenVM: ViewModel() {
    val TAG = "OtpVerificationScreenVM";

    private val apiClient = ApiClient.getInstance();

    private val _verifyOtpResponse = MutableStateFlow<ApiResponse<ResponseWrapper<VerifyOtpResponse>>?>(null)
    val verifyOtpResponse: StateFlow<ApiResponse<ResponseWrapper<VerifyOtpResponse>>?> = _verifyOtpResponse.asStateFlow()

    fun onBackPress(index: Int, otpValue: MutableList<String>, focusRequester: List<FocusRequester>) {
        if (index > 0) {
            focusRequester[index - 1].requestFocus()
            otpValue[index - 1] = ""
        }
    }

    fun verifyOtp(requestId:String, mobileNumber: String, otp: String) {

        viewModelScope.launch {
            try {
                _verifyOtpResponse.value = ApiResponse.Loading

                FirebaseUtils.deleteFCMToken()
                var fcmToken = FirebaseUtils.generateFCMToken()

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.VERIFY_OTP) {
                        setBody(
                            mapOf(
                                "requestId" to requestId,
                                "otp" to otp,
                                "mobile_number" to mobileNumber,
                                "fcmToken" to fcmToken
                            )
                        )
                    }

                val result: ResponseWrapper<VerifyOtpResponse> = response.body()

                if(result.status) {
                    _verifyOtpResponse.value = ApiResponse.Success(result)
                } else {

                    _verifyOtpResponse.value = ApiResponse.Error(result.message)
                }

            } catch (e:Exception) {
                _verifyOtpResponse.value = ApiResponse.Error("Exception : ${e.message.toString()}")
            }
        }
    }
}