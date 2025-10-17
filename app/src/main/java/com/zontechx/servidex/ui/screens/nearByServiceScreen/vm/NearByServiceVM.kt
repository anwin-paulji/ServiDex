package com.zontechx.servidex.ui.screens.nearByServiceScreen.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.response.Bookings
import com.zontechx.servidex.model.response.ServiceDetailsResponse
import com.zontechx.servidex.model.response.UserBookingsData
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

class NearByServiceVM: ViewModel() {

    val TAG = "NearByServiceVM"
    private val apiClient = ApiClient.getInstance();
    /*****************************************************************************************/
    /***************************** Booking list Pagination ***********************************/

    private val _nearByListUiState: MutableStateFlow<ApiResponse<List<ServiceDetailsResponse>>> = MutableStateFlow<ApiResponse<List<ServiceDetailsResponse>>>(ApiResponse.Initial)
    val nearByListState: StateFlow<ApiResponse<List<ServiceDetailsResponse>>> = _nearByListUiState.asStateFlow()
    val nearByList: StateFlow<List<ServiceDetailsResponse>> =
        nearByListState.map { (it as? ApiResponse.Success)?.data ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var nearByListCurrentPage = 1
    private val nearByListPageSize = 10
    private var nearByListAll = mutableListOf<ServiceDetailsResponse>()
    var hasMore = mutableStateOf(true)
    var isLoadingMore = mutableStateOf(false)

    fun loadInitial(latitude: Double, longitude: Double) {
        Log.e(TAG, "loadInitial: Trigger load initial")
        nearByListCurrentPage = 1
        viewModelScope.launch {
            _nearByListUiState.value = ApiResponse.Loading
            try {

                val requestBody = buildJsonObject {
                    put("page", nearByListCurrentPage)
                    put("pageCount", nearByListPageSize)
                    put("latitude", latitude)
                    put("longitude", longitude)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.GET_NEAR_BY_SERVICE_LIST) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<List<ServiceDetailsResponse>> = response.body()
                if (result.status && result.data != null) {
                    nearByListAll.clear()
                    nearByListAll.addAll(result.data)
                    hasMore.value = result.data.isNotEmpty()
                    _nearByListUiState.value =  ApiResponse.Success(nearByListAll.toList())
                } else {
                    _nearByListUiState.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                _nearByListUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMore() {
        Log.e(TAG, "loadMore: Trigger load more")
        if (!hasMore.value || isLoadingMore.value) return

        viewModelScope.launch {
            try {

                val requestBody = buildJsonObject {
                    put("page", ++nearByListCurrentPage)
                    put("pageCount", nearByListPageSize)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.GET_NEAR_BY_SERVICE_LIST) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<List<ServiceDetailsResponse>> = response.body()
                if (result.status && result.data != null) {
                    nearByListAll.addAll(result.data)
                    hasMore.value = result.data.isNotEmpty()
                    _nearByListUiState.value =
                        ApiResponse.Success(nearByListAll.toList()) // emit domain list
                } else {
                    hasMore.value = false
                    _nearByListUiState.value =
                        ApiResponse.Success(nearByListAll.toList()) // emit domain list
                }
            } catch (e: Exception) {
                _nearByListUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            } finally {
                isLoadingMore.value = false
            }
        }
    }
}