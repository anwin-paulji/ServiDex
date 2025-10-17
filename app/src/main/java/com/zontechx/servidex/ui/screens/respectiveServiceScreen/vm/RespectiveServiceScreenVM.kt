package com.zontechx.servidex.ui.screens.respectiveServiceScreen.vm

import android.util.Log
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
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlin.collections.plus

class RespectiveServiceScreenVM : ViewModel() {

    val TAG = "RespectiveServiceScreenVM"
    private val apiClient = ApiClient.getInstance();
    private var _serviceListResponse: MutableStateFlow<ApiResponse<List<ServiceData>>> =
        MutableStateFlow(ApiResponse.Initial)
    val serviceListResponse = _serviceListResponse.asStateFlow()

    private val _serviceList = MutableStateFlow<List<ServiceData>>(emptyList())
    val serviceList: StateFlow<List<ServiceData>> = _serviceList.asStateFlow()

    var isLoading = false
    private var hasMoreData = true
    private var currentPage = 1
    private var lastCategoryId: String? = null
    private val pageSize = 500

    /**
     * Call this when selecting a new service category,
     * or before loading, to reset the state for a new category ID.
     */
    fun resetServiceCategory(categoryId: String) {
        currentPage = 1
        hasMoreData = true
        isLoading = false
        _serviceList.value = emptyList()
        _serviceListResponse.value = ApiResponse.Initial
        lastCategoryId = categoryId
    }

    /**
     * Loads next page of data for specified service category.
     * If switching categories, will auto-reset state.
     *
     * Usage:
     *   Call resetServiceCategory(id) when changing category.
     *   Then call specificServiceList(id) to paginate.
     */
    fun specificServiceList(serviceId: String) {
        Log.e(TAG, "Call #1: specificServiceList")
        // If switching category, reset pagination state
        if (lastCategoryId == null || lastCategoryId != serviceId) {
            resetServiceCategory(serviceId)
        }
        if (isLoading || !hasMoreData) return
        isLoading = true
        _serviceListResponse.value = ApiResponse.Loading
        Log.e(TAG, "Call #2")

        val request = ServiceListRequest(
            page = currentPage,
            pageSize = pageSize,
            categoryId = serviceId
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e(TAG, "Call #3")
                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.RESPECTIVE_SERVICE_LIST) {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }
                Log.e(TAG, "Call #4")
                val result: ResponseWrapper<List<ServiceData>> = response.body()
                if (result.status) {
                    val newData = result.data ?: emptyList()
                    // Append to existing list (which may be empty following reset)
                    val updatedList = _serviceList.value + newData
                    _serviceList.value = updatedList
                    _serviceListResponse.value = ApiResponse.Success(updatedList)
                    // Detect if more data is available (if returned == pageSize)
                    hasMoreData = newData.size == pageSize
                    if (hasMoreData) currentPage++
                } else {
                    _serviceListResponse.value = ApiResponse.Error(result.message)
                    hasMoreData = false
                }
            } catch (e: CancellationException) {
                // Cancellation is expected on coroutine cancel, do NOT treat as error
                Log.e(TAG, "Coroutine cancelled: ${e.message}")
                isLoading = false
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                _serviceListResponse.value = ApiResponse.Error(e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
}

@Serializable
data class ServiceListRequest(
    val page: Int,
    val pageSize: Int,
    val categoryId: String
)