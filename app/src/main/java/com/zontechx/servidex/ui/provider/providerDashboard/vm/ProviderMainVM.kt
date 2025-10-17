package com.zontechx.servidex.ui.provider.providerDashboard.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.ConfirmationSheetState
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.model.response.Bookings
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

class ProviderMainVM : ViewModel() {
    private val TAG = "ProviderMainVM"
    private val apiClient = ApiClient.getInstance();

    /////
    val TAG_BOOKINGS_PAGINATION = "TAG_PROVIDER_BOOKINGS_PAGINATION"
    private val _bookingsUiState: MutableStateFlow<ApiResponse<List<Bookings>>> =
        MutableStateFlow<ApiResponse<List<Bookings>>>(ApiResponse.Initial)
    val bookingsUiState: StateFlow<ApiResponse<List<Bookings>>> = _bookingsUiState.asStateFlow()
    val bookingsList: StateFlow<List<Bookings>> =
        bookingsUiState.map { (it as? ApiResponse.Success)?.data ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var bookingsCurrentPage = 1
    private val bookingsPageSize = 10
    private var currentBookingType = BookingStatus.ALL
    private var allBookings = mutableListOf<Bookings>()

    var hasMore = mutableStateOf(true)
    var isLoadingMore = mutableStateOf(false)

    fun loadInitial(bookingType: BookingStatus = currentBookingType) {
        Log.e(TAG, "loadInitial: Trigger load initial")
        currentBookingType = bookingType
        bookingsCurrentPage = 1
        viewModelScope.launch {
            _bookingsUiState.value = ApiResponse.Loading
            try {

                val requestBody = buildJsonObject {
                    put("page", bookingsCurrentPage)
                    put("pageCount", bookingsPageSize)
                    put("bookingStatus", currentBookingType.value)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.PROVIDER_BOOKINGS) {
                        setBody(requestBody)
                    }
                Log.e(TAG_BOOKINGS_PAGINATION, "Bookings Response: ${response}")
                val result: ResponseWrapper<UserBookingsData> = response.body()
                if (result.status && result.data != null) {
                    allBookings.clear()
                    allBookings.addAll(result.data.bookings)
                    hasMore.value = result.data.bookings.isNotEmpty()
                    _bookingsUiState.value =
                        ApiResponse.Success(allBookings.toList()) // emit domain list
                    Log.e(TAG_BOOKINGS_PAGINATION, "Bookings loaded: ${allBookings.size}")
                } else {
                    _bookingsUiState.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                Log.e(TAG_BOOKINGS_PAGINATION, "Bookings Exception: ${e.message}")
                _bookingsUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMore() {
        Log.e(TAG, "loadMore: Trigger load more")
        if (!hasMore.value || isLoadingMore.value) return

        viewModelScope.launch {
            try {

                val requestBody = buildJsonObject {
                    put("page", ++bookingsCurrentPage)
                    put("pageCount", bookingsPageSize)
                    put("bookingStatus", currentBookingType.value)
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.PROVIDER_BOOKINGS) {
                        setBody(requestBody)
                    }
                val result: ResponseWrapper<UserBookingsData> = response.body()
                if (result.status && result.data != null) {
                    allBookings.addAll(result.data.bookings)
                    hasMore.value = result.data.bookings.isNotEmpty()
                    _bookingsUiState.value =
                        ApiResponse.Success(allBookings.toList()) // emit domain list
                    Log.e(TAG_BOOKINGS_PAGINATION, "Bookings loaded: ${allBookings.size}")
                } else {
                    hasMore.value = false
                    _bookingsUiState.value =
                        ApiResponse.Success(allBookings.toList()) // emit domain list
                }
            } catch (e: Exception) {
                Log.e(TAG_BOOKINGS_PAGINATION, "Bookings Exception: ${e.message}")
                _bookingsUiState.value = ApiResponse.Error(e.message ?: "Unknown error")
            } finally {
                isLoadingMore.value = false
            }
        }
    }

    //===============================================================//
    // Expose a state for whether the sheet should be shown
    private val _showServiceDetailsSheet = MutableStateFlow(false)
    val showServiceDetailsSheet: StateFlow<Boolean> = _showServiceDetailsSheet.asStateFlow()

    private val _selectedBookingDetails = MutableStateFlow<Bookings?>(null)
    val selectedBookingDetails: StateFlow<Bookings?> = _selectedBookingDetails.asStateFlow()

    // Sheet control functions
    fun showSheet() {
        _showServiceDetailsSheet.value = true
    }

    fun hideSheet() {
        _showServiceDetailsSheet.value = false
        _selectedBookingDetails.value = null
    }

    fun onOrderClick(booking: String) {
        _selectedBookingDetails.value = bookingsList.value.find { it._id == booking }
        showSheet()
    }

    //===============================================================//
    //=====================Update Booking Status=====================//
    var selectedReason = MutableStateFlow<String>("")
    // Handle the positive action for the ServiceDetailsSheet
    fun handleServiceDetailsPositiveAction() {
        val currentBookingStatus = selectedBookingDetails.value?.bookingStatus
        when (currentBookingStatus) {
            BookingStatus.PENDING.value -> {
                // Next status of PENDING in positive approach is CONFIRMED
                openConfirmationDialog(BookingStatus.CONFIRMED)
            }

            BookingStatus.CONFIRMED.value -> {
                // Handle "Complete Job" action
                openConfirmationDialog(BookingStatus.IN_PROGRESS)
            }

            BookingStatus.IN_PROGRESS.value -> {
                // Handle "Complete Job" action
                openConfirmationDialog(BookingStatus.COMPLETED)
            }

            BookingStatus.COMPLETED.value -> {
                // Handle "Rate Customer" action
            }

            else -> {
                // No action needed
                closeConfirmationSheet()
            }
        }
    }

    // Handle the negative action for the ServiceDetailsSheet
    fun handleServiceDetailsNegativeAction() {
        when (selectedBookingDetails.value?.bookingStatus) {
            BookingStatus.PENDING.value -> {
                // Handle "Start Job" action
                openConfirmationDialog(BookingStatus.REJECTED)
            }

            BookingStatus.CONFIRMED.value -> {
                // Handle "Complete Job" action
                openConfirmationDialog(BookingStatus.CANCELLED)
            }

            BookingStatus.IN_PROGRESS.value -> {
                // Handle "Complete Job" action
                openConfirmationDialog(BookingStatus.CANCELLED)
            }

            BookingStatus.COMPLETED.value -> {
                // Handle "Rate Customer" action
            }

            else -> {
                // No action needed
            }
        }
    }

    private val _confirmationSheetState = MutableStateFlow(ConfirmationSheetState())
    val confirmationSheetState = _confirmationSheetState.asStateFlow()

    fun onReasonChange(newReason: String) {
        selectedReason.value = newReason
    }

    fun openConfirmationDialog(newBookingStatus: BookingStatus) {
        selectedReason.value = "" // Reset the reason field
        val state = when (newBookingStatus) {
            BookingStatus.CONFIRMED -> ConfirmationSheetState(
                newBookingStatus = newBookingStatus,
                title = "Accept Booking",
                description = "Are you sure you want to accept this booking?",
                positiveText = "Confirm",
                negativeText = "Cancel",
                visible = true
            )

            BookingStatus.REJECTED -> ConfirmationSheetState(
                newBookingStatus = newBookingStatus,
                title = "Reject Booking",
                description = "Are you sure you want to reject this booking?",
                positiveText = "Reject",
                negativeText = "Cancel",
                visible = true
            )

            BookingStatus.IN_PROGRESS -> {
                ConfirmationSheetState(
                    newBookingStatus = newBookingStatus,
                    title = "Start Work",
                    description = "Are you sure you want to start work on this booking?",
                    positiveText = "Start",
                    negativeText = "Cancel",
                    visible = true
                )
            }

            BookingStatus.COMPLETED -> {
                ConfirmationSheetState(
                    newBookingStatus = newBookingStatus,
                    title = "Complete Booking",
                    description = "Are you sure you want to mark this booking as completed?",
                    positiveText = "Complete",
                    negativeText = "Cancel",
                    visible = true
                )
            }

            BookingStatus.CANCELLED -> {
                ConfirmationSheetState(
                    newBookingStatus = newBookingStatus,
                    title = "Cancel Booking",
                    description = "Are you sure you want to cancel this booking?",
                    positiveText = "Cancel Booking",
                    negativeText = "Close",
                    visible = true
                )
            }

            else -> ConfirmationSheetState(visible = false)
        }
        _confirmationSheetState.value = state
    }

    fun closeConfirmationSheet() {
        _confirmationSheetState.value = ConfirmationSheetState(visible = false)
    }

    fun onPositiveConfirmationAction() {
        val newBookingStatus: BookingStatus = confirmationSheetState.value.newBookingStatus
        updateBookingStatus(newStatus = newBookingStatus)
        closeConfirmationSheet()
    }

    private val _bookingStatusUpdateResponse =
        MutableStateFlow<ApiResponse<ResponseWrapper<Bookings>>>(ApiResponse.Initial)
    val bookingStatusUpdateResponse: StateFlow<ApiResponse<ResponseWrapper<Bookings>>> =
        _bookingStatusUpdateResponse.asStateFlow()

    fun updateBookingStatus(newStatus: BookingStatus) {

        _bookingStatusUpdateResponse.value = ApiResponse.Loading
        val bookingId = selectedBookingDetails.value?._id ?: return

        viewModelScope.launch {
            try {
                val requestBody = buildJsonObject {
                    put("bookingId", bookingId)
                    put("userType", "PROVIDER")
                    put("bookingStatus", newStatus.value)
                    if(newStatus == BookingStatus.CANCELLED || newStatus == BookingStatus.REJECTED) {
                        put("reason", selectedReason.value)
                    }
                }

                val response =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.UPDATE_BOOKING_STATUS) {
                        setBody(requestBody)
                    }

                val result: ResponseWrapper<Bookings> = response.body()

                if (result.status) {
                    // Handle success
                    _bookingStatusUpdateResponse.value = ApiResponse.Success(result)

                    if (result.data != null) {
                        updateExistingList(result.data)
                    }

                } else {
                    // Handle error
                    _bookingStatusUpdateResponse.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                // Handle exception
                _bookingStatusUpdateResponse.value = ApiResponse.Error(e.message ?: "Unknown error")
            }
        }
        //===============================================================//
    }

    fun updateExistingList(updatedBooking: Bookings) {
        _selectedBookingDetails.value = updatedBooking
        val currentList = (bookingsUiState.value as? ApiResponse.Success)?.data ?: emptyList()
        val newList = currentList.map {
            if (it._id == updatedBooking._id) updatedBooking else it
        }
        _bookingsUiState.value = ApiResponse.Success(newList)
    }
}
