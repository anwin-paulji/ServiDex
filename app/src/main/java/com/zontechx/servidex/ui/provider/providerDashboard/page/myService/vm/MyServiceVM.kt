package com.zontechx.servidex.ui.provider.providerDashboard.page.myService.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.response.ServiceData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class MyServiceVM : ViewModel() {
    private val TAG = "MyServiceVM"
    private val apiClient = ApiClient.getInstance();
    /**********************************************************************************************/
    /**********************************************************************************************/
    private val _myServiceUiState: MutableStateFlow<ApiResponse<List<ServiceData>>> =
        MutableStateFlow<ApiResponse<List<ServiceData>>>(ApiResponse.Initial)
    val myServiceUiState: StateFlow<ApiResponse<List<ServiceData>>> = _myServiceUiState.asStateFlow()
    val myServiceList: StateFlow<List<ServiceData>> =
        myServiceUiState.map { (it as? ApiResponse.Success)?.data ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var myServoceListCurrentPage = 1
    private val myServoceListPageSize = 10
    private var myServoceListAll = mutableListOf<ServiceData>()
    var hasMore = mutableStateOf(true)
    var isLoadingMore = mutableStateOf(false)

    fun loadInitial() {
        Log.e(TAG, "loadInitial: Trigger load initial")
        myServoceListCurrentPage = 1
        viewModelScope.launch {
            _myServiceUiState.value = ApiResponse.Loading
            try {

                val requestBody = buildJsonObject {
                    put("page", myServoceListCurrentPage)
                    put("pageCount", myServoceListPageSize)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.MY_SERVICE_LIST) {
                        setBody(requestBody)
                    }
                Log.e(TAG, "My Service Response: ${response}")
                val result: ResponseWrapper<List<ServiceData>> = response.body()
                if (result.status && result.data != null) {
                    myServoceListAll.clear()
                    myServoceListAll.addAll(result.data)
                    hasMore.value = result.data.isNotEmpty()
                    _myServiceUiState.value =
                        ApiResponse.Success(myServoceListAll.toList()) // emit domain list
                    Log.e(TAG, "My Service loaded: ${myServoceListAll.size}")
                } else {
                    _myServiceUiState.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Bookings Exception: ${e.message}")
                _myServiceUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMore() {
        Log.e(TAG, "loadMore: Trigger load more")
        if (!hasMore.value || isLoadingMore.value) return

        viewModelScope.launch {
            try {

                val requestBody = buildJsonObject {
                    put("page", ++myServoceListCurrentPage)
                    put("pageCount", myServoceListPageSize)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.MY_SERVICE_LIST) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<List<ServiceData>> = response.body()
                if (result.status && result.data != null) {
                    myServoceListAll.addAll(result.data)
                    hasMore.value = result.data.isNotEmpty()
                    _myServiceUiState.value =
                        ApiResponse.Success(myServoceListAll.toList()) // emit domain list
                    Log.e(TAG, "My Service loaded: ${myServoceListAll.size}")
                } else {
                    hasMore.value = false
                    _myServiceUiState.value =
                        ApiResponse.Success(myServoceListAll.toList()) // emit domain list
                }
            } catch (e: Exception) {
                Log.e(TAG, "My Service Exception: ${e.message}")
                _myServiceUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            } finally {
                isLoadingMore.value = false
            }
        }
    }

    fun onMyServiceSelection(serviceId: String) {}
    /**********************************************************************************************/
    /**********************************************************************************************/
}