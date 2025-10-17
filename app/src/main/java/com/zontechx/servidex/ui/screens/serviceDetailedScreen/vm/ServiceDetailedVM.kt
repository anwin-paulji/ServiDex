package com.zontechx.servidex.ui.screens.serviceDetailedScreen.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.Bookings
import com.zontechx.servidex.model.response.RatingReview
import com.zontechx.servidex.model.response.RatingReviewResponse
import com.zontechx.servidex.model.response.ServiceDetailsResponse
import com.zontechx.servidex.model.response.UserBookingsData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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

class ServiceDetailedVM: ViewModel() {

    private val TAG = "ServiceDetailedVM"

    var screenSource by mutableStateOf(AppConstant.ScreenSource.FROM_CATEGORY)

    private val apiClient = ApiClient.getInstance()

    private val _serviceDetails = MutableStateFlow<ServiceDetailsResponse?>(null)
    val serviceDetails = _serviceDetails.asStateFlow()

    private val _serviceDetailResponse = MutableStateFlow<ApiResponse<ResponseWrapper<ServiceDetailsResponse>>>(ApiResponse.Initial)
    val serviceDetailResponse = _serviceDetailResponse.asStateFlow()

    var selectedServiceId by mutableStateOf("")

    fun getServiceById(serviceId: String) {
        selectedServiceId = serviceId
        viewModelScope.launch {
            try {
                _serviceDetailResponse.value = ApiResponse.Loading
                Log.e(TAG, "Request Body :  serviceId > $serviceId")
                val response: HttpResponse = apiClient.client.get(AppConfig.BASE_URL + ApiEndpoint.SERVICE_DETAILS) {
                    parameter("serviceId", serviceId) // <-- add query param
                }

                val result: ResponseWrapper<ServiceDetailsResponse> = response.body()

                if (result.status == true) {
                    _serviceDetailResponse.value = ApiResponse.Success(result)
                    _serviceDetails.value = result.data
                } else {
                    _serviceDetailResponse.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception : ${e.message.toString()}")
                _serviceDetailResponse.value =
                    ApiResponse.Error("Exception : ${e.message.toString()}")
            }
        }
    }

    /*************************************************************************/
    /****************************** Review ***********************************/
    private val _reviewsUiState: MutableStateFlow<ApiResponse<List<RatingReview>>> = MutableStateFlow<ApiResponse<List<RatingReview>>>(ApiResponse.Initial)
    val reviewsUiState: StateFlow<ApiResponse<List<RatingReview>>> = _reviewsUiState.asStateFlow()
    val reviewsList: StateFlow<List<RatingReview>> =
        reviewsUiState.map { (it as? ApiResponse.Success)?.data ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var reviewsCurrentPage = 1
    private val reviewsPageSize = 10
    private var allReviews = mutableListOf<RatingReview>()
    var hasMore = mutableStateOf(true)
    var isLoadingMore = mutableStateOf(false)

    fun loadReviewInitial(serviceId: String) {
        Log.e(TAG, "loadReviewInitial: Trigger load initial")
        reviewsCurrentPage = 1
        viewModelScope.launch {
            _reviewsUiState.value = ApiResponse.Loading
            try {

                val requestBody = buildJsonObject {
                    put("page", reviewsCurrentPage)
                    put("pageCount", reviewsPageSize)
                    put("serviceId", serviceId)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.SERVICE_REVIEWS_LIST) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<RatingReviewResponse> = response.body()
                if (result.status && result.data != null) {
                    allReviews.clear()
                    allReviews.addAll(result.data.reviews)
                    hasMore.value = result.data.reviews.isNotEmpty()
                    _reviewsUiState.value =
                        ApiResponse.Success(allReviews.toList()) // emit domain list
                } else {
                    _reviewsUiState.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                _reviewsUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadReviewMore() {
        Log.e(TAG, "loadReviewMore: Trigger load more")
        if (!hasMore.value || isLoadingMore.value) return

        viewModelScope.launch {
            try {

                val requestBody = buildJsonObject {
                    put("page", ++reviewsCurrentPage)
                    put("pageCount", reviewsPageSize)
                    put("serviceId", selectedServiceId)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.SERVICE_REVIEWS_LIST) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<RatingReviewResponse> = response.body()
                if (result.status && result.data != null) {
                    allReviews.addAll(result.data.reviews)
                    hasMore.value = result.data.reviews.isNotEmpty()
                    _reviewsUiState.value =
                        ApiResponse.Success(allReviews.toList()) // emit domain list
                } else {
                    hasMore.value = false
                    _reviewsUiState.value =
                        ApiResponse.Success(allReviews.toList()) // emit domain list
                }
            } catch (e: Exception) {
                _reviewsUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            } finally {
                isLoadingMore.value = false
            }
        }
    }
    /*************************************************************************/
    /*************************************************************************/
}