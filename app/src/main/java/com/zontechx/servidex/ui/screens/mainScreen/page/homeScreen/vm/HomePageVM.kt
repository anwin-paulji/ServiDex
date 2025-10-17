package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.response.UserData
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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class HomePageVM : ViewModel() {

    val TAG = "HomePageVM"
    private val apiClient = ApiClient.getInstance()

    private val _feedbackUpdatedResponse = MutableSharedFlow<ApiResponse<JsonElement>>()
    val feedbackUpdatedResponse: SharedFlow<ApiResponse<JsonElement>> =
        _feedbackUpdatedResponse.asSharedFlow()

    fun sendFeedback(level: Int, feedback: String) {
        viewModelScope.launch {
            try {

                _feedbackUpdatedResponse.emit(ApiResponse.Loading)

                val requestBody = buildJsonObject {
                    put("level", level)
                    put("feedback", feedback)
                }

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.USER_FEEDBACK) {
                        setBody(requestBody)
                    }

                val result: ResponseWrapper<JsonElement> = response.body()

                if (result.status) {

                    if (result.data == null) {
                        _feedbackUpdatedResponse.emit(ApiResponse.Error("No Data Found"))
                        return@launch
                    }
                    _feedbackUpdatedResponse.emit(ApiResponse.Success(result.data))
                } else {
                    _feedbackUpdatedResponse.emit(ApiResponse.Error(result.message))
                }

            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
                _feedbackUpdatedResponse.emit(ApiResponse.Error(exception.message.toString()))
            }
        }
    }
}