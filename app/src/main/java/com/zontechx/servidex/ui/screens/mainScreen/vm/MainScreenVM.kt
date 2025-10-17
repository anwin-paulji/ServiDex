package com.zontechx.servidex.ui.screens.mainScreen.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.data.model.response.HomeScreenResponse
import com.zontechx.servidex.model.ConfirmationSheetState
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.model.response.Bookings
import com.zontechx.servidex.model.response.LocationDataItem
import com.zontechx.servidex.model.response.UserBookingsData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class MainScreenVM : ViewModel() {

    var TAG = "MainScreenVM"
    val apiClient = ApiClient.getInstance()

    val _dashboardData = MutableStateFlow<ApiResponse<ResponseWrapper<HomeScreenResponse>>>(ApiResponse.Initial)
    var dashboardData = _dashboardData.asStateFlow()
    var dashboardApiInitialized: Boolean = false

    private val _categoryList: MutableStateFlow<ApiResponse<List<CategoryListItem?>>> =
        MutableStateFlow(ApiResponse.Initial)
    val categoryList: StateFlow<ApiResponse<List<CategoryListItem?>>> = _categoryList.asStateFlow()

    fun retryDashboardApi(lat: Double, long: Double) {
        dashboardApiInitialized = false
        getDashboardData(lat, long)
    }

    fun getDashboardData(lat: Double, long: Double) {

        if (dashboardApiInitialized) {
            return
        }
        dashboardApiInitialized = true

        _dashboardData.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                var response =
                    apiClient.client.get(AppConfig.BASE_URL + ApiEndpoint.DASHBOARD_DATA) {
                        setBody(
                            mapOf(
                                "location" to mapOf(
                                    "lat" to lat, "long" to long
                                )
                            )
                        )
                    }

                val result: ResponseWrapper<HomeScreenResponse> = response.body()

                if (result.status == true) {
                    Log.e(TAG, "Success: ${result.data}")
                    _dashboardData.value = ApiResponse.Success(result)
                } else {
                    Log.e(TAG, "Failure: ${result.message}")
                    _dashboardData.value = ApiResponse.Error(result.message)
                }

            } catch (exception: kotlinx.coroutines.CancellationException) {
                Log.e(TAG, "Coroutine cancelled: ${exception.message}")
                dashboardApiInitialized = false // Reset flag to allow retry
                // Don't emit error state for cancellation
            } catch (exception: Exception) {
                Log.e(TAG, "Exception: ${exception.message}")
                dashboardApiInitialized = false // Reset flag to allow retry
                _dashboardData.value = ApiResponse.Error("Exception : ${exception.message}")
            }
        }
    }

    // Api Call
    fun getCategoryList() {
        viewModelScope.launch {
            try {
                _categoryList.value = ApiResponse.Loading
                var response = apiClient.client.get(AppConfig.BASE_URL + ApiEndpoint.CATEGORY_LIST)
                val result: ResponseWrapper<List<CategoryListItem?>> = response.body()

                if (result.status) {

                    if (result.data == null) {
                        _categoryList.value = ApiResponse.Error("No Data Found")
                        return@launch
                    }
                    _categoryList.value = ApiResponse.Success(result.data)
                } else {
                    _categoryList.value = ApiResponse.Error(result.message)
                }

            } catch (exception: Exception) {
                _categoryList.value = ApiResponse.Error(exception.message.toString())
            }
        }
    }

    //------------------------------//
    private val _showLocationSheet = MutableStateFlow(false)
    val showLocationSheet: StateFlow<Boolean> = _showLocationSheet
    fun onClickLocationSheet(status: Boolean) {
        _showLocationSheet.value = status
    }
    //------------------------------//
    // Location list Pagination
    private val _locationResponse: MutableStateFlow<ApiResponse<List<LocationDataItem>>> =
        MutableStateFlow(ApiResponse.Initial)
    val locationResponse: StateFlow<ApiResponse<List<LocationDataItem>>> = _locationResponse.asStateFlow()

    private val _locationList = MutableStateFlow<List<LocationDataItem>>(emptyList())
    val locationList: StateFlow<List<LocationDataItem>> = _locationList.asStateFlow()

    var isLoading = false
    private var hasMoreData = true
    private var currentPage = 1
    private val pageSize = 500

    fun loadNextPage() {
        if (isLoading || !hasMoreData) return
        isLoading = true
        _locationResponse.value = ApiResponse.Loading

        viewModelScope.launch {
            try {
                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.LOCATION_LIST) {
                        setBody(
                            mapOf(
                                "page" to currentPage,
                                "pageCount" to pageSize,
                                // "searchText" to searchText
                            )
                        )
                    }

                val result: ResponseWrapper<List<LocationDataItem>> = response.body()

                if (result.status) {
                    val newData = result.data ?: emptyList()

                    // ✅ Append to existing list
                    val updatedList = _locationList.value + newData
                    _locationList.value = updatedList

                    // ✅ Emit success only with newData (or whole updatedList depending on your UI)
                    _locationResponse.value = ApiResponse.Success(newData)

                    // ✅ Check if there is more data
                    if (newData.size < pageSize) {
                        hasMoreData = false
                    } else {
                        currentPage++
                    }
                } else {
                    _locationResponse.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                _locationResponse.value = ApiResponse.Error(e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
}