package com.zontechx.servidex.ui.screens.booking.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.model.response.ServiceDetailsResponse
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.utils.AppConfig
import com.zontechx.servidex.utils.CurrentAddress
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ViewModel
class BookingVM : ViewModel() {

    private val TAG = "BookingVM"

    private val apiClient = ApiClient.getInstance();

    var showGoogleMapSheet = MutableStateFlow(false)
    var serviceLocation = MutableStateFlow<CurrentAddress?>(null)

    var contactPersonName = MutableStateFlow("")
    var contactPersonNumber = MutableStateFlow("")
    var additionalNotes = MutableStateFlow("")
    var selectedDate = MutableStateFlow("0000-00-00")

    var timeFrom = MutableStateFlow("00:00")
    var timeTo = MutableStateFlow("00:00")

    var serviceDetails = MutableStateFlow<ServiceDetailsResponse?>(null)

    fun setServiceJson(json: String) {
        /**
         * We have used decodeFromString to convert json string to object
         * Reason : We are passing data from one screen to another screen using navigation
         * If we use Serialization or Parcelable we need to be more careful about the data type
         * */
        serviceDetails.value = Json.decodeFromString<ServiceDetailsResponse>(json)
    }

    fun showGoogleMapBottomSheet(show: Boolean) {
        showGoogleMapSheet.value = show
    }

    fun addServiceLocation(address: CurrentAddress) {
        serviceLocation.value = address
    }

    private val _bookingResponse = MutableSharedFlow<ApiResponse<ResponseWrapper<BookingResponse>>>()
    val bookingResponse = _bookingResponse.asSharedFlow()

    fun onBookServiceClick() {
        viewModelScope.launch {
            bookService()
        }
    }

    /**
     * Do API call for Booking the service
     * */
    suspend fun bookService() {
        if(serviceDetails.value?._id == "") {
            _bookingResponse.emit(ApiResponse.Error("Service details not found"))
            return
        }

        if(serviceLocation.value == null) {
            _bookingResponse.emit(ApiResponse.Error("Service location not found"))
            return
        }

        if(selectedDate.value == "0000-00-00") {
            _bookingResponse.emit(ApiResponse.Error("Please select date"))
            return
        }

        if(timeFrom.value == "00:00") {
            _bookingResponse.emit(ApiResponse.Error("Please select time from"))
            return
        }

//        if(timeTo.value == "00:00") {
//            _bookingResponse.emit(ApiReasponse.Failed("Please select time to"))
//            return
//        }
//
//        if(contactPersonName.value.isEmpty()) {
//            _bookingResponse.emit(ApiReasponse.Failed("Please enter contact person name"))
//            return
//        }
//
//        if(contactPersonNumber.value.isEmpty()) {
//            _bookingResponse.emit(ApiReasponse.Failed("Please enter contact person number"))
//            return
//        }
//
//        // Validate Mobile Number
//        if(contactPersonNumber.value.length != 10) {
//            _bookingResponse.emit(ApiReasponse.Failed("Please enter valid contact person number"))
//            return
//        }

        val requestBody = BookingRequest(
            serviceId = serviceDetails.value?._id!!,
            specialInstructions = additionalNotes.value,
            address = Address(
                fullAddress = serviceLocation.value?.addressLine,
                latitude = serviceLocation.value?.latitude,
                longitude = serviceLocation.value?.longitude
            ),
            serviceDate = selectedDate.value,
            bookingTime = BookingTime(
                startTime = timeFrom.value,
                endTime = timeTo.value
            ),
            contactDetails = ContactDetails(
                name = contactPersonName.value,
                mobileNumber = contactPersonNumber.value
            )
        )

        viewModelScope.launch {
            try {
                _bookingResponse.emit(ApiResponse.Loading)

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.BOOK_SERVICE) {
                        setBody(requestBody)
                    }

                Log.e(TAG, "result: $response")

                val result: ResponseWrapper<BookingResponse> = response.body()

                if(result.status) {
                    _bookingResponse.emit(ApiResponse.Success(result))
                } else {
                    _bookingResponse.emit(ApiResponse.Error(result.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exceptionn:  ${e.message.toString()}")
                _bookingResponse.emit(ApiResponse.Error("Exceptionn : ${e.message.toString()}"))
            }
        }
    }

    fun onAdditionalNotesChange(value: String) {
        additionalNotes.value = value
    }
    
    fun onContactPersonNameChange(value: String) {
        contactPersonName.value = value
    }
    
    fun onContactPersonNumberChange(value: String) {
        contactPersonNumber.value = value
    }

    fun onTimeFromSelected(value: String) {
        timeFrom.value = value
    }

    fun onTimeToSelected(value: String) {
        timeTo.value = value
    }
    
    fun onDateSelected(value: String) {
        selectedDate.value = value
    }
}

//val selectedDate = LocalDate.of(2025, 8, 12)  // from date picker
//val startTime = LocalTime.of(10, 0)           // from time slot
//val endTime = LocalTime.of(12, 0)
//
//val zoneOffset = ZoneOffset.ofHoursMinutes(5, 30) // IST
//
//val startDateTime = LocalDateTime.of(selectedDate, startTime)
//val endDateTime = LocalDateTime.of(selectedDate, endTime)
//
//val startIso = startDateTime.atOffset(zoneOffset).toString()
//val endIso = endDateTime.atOffset(zoneOffset).toString()

@Serializable
data class BookingRequest(
    val serviceId: String,
    val specialInstructions: String?,
    val address: Address,
    val serviceDate: String,
    val bookingTime: BookingTime,
    val contactDetails: ContactDetails
)

@Serializable
data class Address(
    val fullAddress: String?,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class BookingTime(
    val startTime: String?,
    val endTime: String?
)

@Serializable
data class ContactDetails(
    val name: String?,
    val mobileNumber: String?
)

@Serializable
data class BookingResponse(
    val serviceId: String? = "",
)