package com.zontechx.servidex.ui.provider.addServiceScreen.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zontechx.servidex.Screen
import com.zontechx.servidex.base.ResponseWrapper
import com.zontechx.servidex.data.model.PricingType
import com.zontechx.servidex.data.model.UnitDuration
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.data.model.response.UpdateUserProfileResponse
import com.zontechx.servidex.model.LocationData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.UiEvent
import com.zontechx.servidex.service.remote.ApiClient
import com.zontechx.servidex.service.remote.ApiEndpoint
import com.zontechx.servidex.ui.provider.addServiceScreen.component.EmergencyServiceOption
import com.zontechx.servidex.utils.AppConfig
import com.zontechx.servidex.utils.CurrentAddress
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

class AddServiceScreenVM() : ViewModel() {

    val tag = "AddServiceVM"

    private val apiClient = ApiClient.getInstance()
    var isStep2FormValid = MutableStateFlow(false)

    val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    //Step 1
    var email = MutableStateFlow<String>("")
    var fullName = MutableStateFlow<String>("")
    var dob = MutableStateFlow<String>("")

    //Step 2
    var serviceTitle = MutableStateFlow<String>("")
    var serviceDescription = MutableStateFlow<String>("")
    var experienceLevel = MutableStateFlow<String>("")
    var experienceLevelNew = MutableStateFlow<String>("")
    var serviceCharges = MutableStateFlow(ChargeBasis())
    var emergencyService = MutableStateFlow(EmergencyServiceOption.NOT_AVAILABLE)
    var showGoogleMapSheet = MutableStateFlow<Boolean>(false)
    var serviceLocation = MutableStateFlow<LocationData?>(null)

    /**
     * Top handle profile completion bottom sheet
     * */
    var showProfileComeletionSheet = MutableStateFlow<Boolean>(false)
    var isDismissTriggered = MutableStateFlow<Boolean>(false)

    var price = MutableStateFlow("")
    var priceType = MutableStateFlow(PricingType.HOURS)
    var unitDuration = MutableStateFlow(UnitDuration.THIRTY.minutes)

    private val _categoryList: MutableStateFlow<ApiResponse<List<CategoryListItem?>>> =
        MutableStateFlow(ApiResponse.Initial)
    val categoryList: StateFlow<ApiResponse<List<CategoryListItem?>>> = _categoryList.asStateFlow()

    private val _selectedCategory: MutableStateFlow<CategoryListItem> =
        MutableStateFlow(CategoryListItem())
    var selectedCategory: StateFlow<CategoryListItem> = _selectedCategory.asStateFlow()

    private val _updatedUserProfileResponse =
        MutableStateFlow<ApiResponse<UpdateUserProfileResponse>>(ApiResponse.Initial)
    val updatedUserProfileResponse: StateFlow<ApiResponse<UpdateUserProfileResponse>> =
        _updatedUserProfileResponse.asStateFlow()

    init {
        observeValidationStep2()
        getCategoryList()
    }


    fun navigateToUploadServiceImageScreen(serviceId: String, screenName: String) {
        viewModelScope.launch {
            val route = Screen.UploadServiceImageScreen.createRoute(
                serviceId = serviceId,
                source = screenName
            )
            _event.emit(UiEvent.Navigate(route))
        }
    }

    fun onFullNameChange(newFullName: String) {
        fullName.value = newFullName
    }

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onDobChange(newDob: String) {
        dob.value = newDob
    }

    fun onServiceTitleChange(newServiceTitle: String) {
        serviceTitle.value = newServiceTitle
    }

    fun onServiceDesicriptionChange(newServiceDescription: String) {
        serviceDescription.value = newServiceDescription
    }

    fun onExperienceLevelChange(newExperienceLevel: String) {
        experienceLevel.value = newExperienceLevel
    }

    // Open Google map > Location pinning bottom sheet
    fun showGoogleMapBottomSheet(show: Boolean) {
        showGoogleMapSheet.value = show
    }

    /**
     * If profile not completed, show profile completion bottom sheet
     * */
    fun showProfileCompletionBottomSheet(show: Boolean) {
        showProfileComeletionSheet.value = show
    }

    fun onDismiss_CompleteUserProfileSheet() {
        isDismissTriggered.value = true
    }

    fun reset_CompleteUserProfileSheet() {
        showProfileComeletionSheet.value = false
        isDismissTriggered.value = false
    }

    fun onPriceChange(newPrice: String) {
        try {
            price.value = newPrice
        } catch (e: Exception) {
            price.value = ""
        }
    }

    fun onPriceTypeChange(newPriceType: PricingType) {
        priceType.value = newPriceType
    }

    fun onUnitDurationChange(newUnitDuration: UnitDuration) {
        unitDuration.value = newUnitDuration.minutes
    }

    fun onEmergencyServiceChange(option: EmergencyServiceOption) {

        when (option) {
            EmergencyServiceOption.AVAILABLE -> {
                emergencyService.value = EmergencyServiceOption.AVAILABLE
            }

            EmergencyServiceOption.NOT_AVAILABLE -> {
                emergencyService.value = EmergencyServiceOption.NOT_AVAILABLE
            }

            else -> emergencyService.value = EmergencyServiceOption.NOT_AVAILABLE
        }
    }

    fun addServiceLocation(address: LocationData) {
        serviceLocation.value = address
    }

    fun observeValidationStep2() {
        viewModelScope.launch {
            combine(
                serviceTitle,
                experienceLevel,
                price,
                emergencyService,
                selectedCategory,
                serviceLocation
            ) { values: Array<Any?> ->
                val serviceTitle = values[0] as String
                val experienceLevel = values[1] as String
                val price = values[2] as String
                val emergencyService = values[3] as EmergencyServiceOption
                val selectedCategory = values[4] as CategoryListItem // guessing your type
                val serviceLocation = values[5] as LocationData?

                serviceTitle.isNotEmpty() &&
                        experienceLevel.isNotEmpty() &&
                        price.isNotEmpty() &&
                        selectedCategory.id != null &&
                        serviceLocation != null &&
                        selectedCategory.subCategory.any { it.isSelected }
            }.collect { isValid ->
                isStep2FormValid.value = isValid
            }
        }
    }


    private var _addServiceResponse =
        MutableStateFlow<ApiResponse<ResponseWrapper<JsonObject>>>(ApiResponse.Initial)
    var addServiceResponse: StateFlow<ApiResponse<ResponseWrapper<JsonObject>>> =
        _addServiceResponse.asStateFlow();

    fun addServiceDetailsAPI() {

        val serviceProfile = ServiceProfile(
            categoryId = selectedCategory.value.id.toString(),
            subCategoryId = selectedCategory.value.subCategory.filter { it.isSelected }[0].id.toString(),
            serviceTitle = serviceTitle.value,
            serviceDescription = serviceDescription.value,
            experienceLevel = experienceLevel.value,
            chargeBasis = ChargeBasis(
                price = price.value.toInt(),
                pricingType = priceType.value.name,
                unitDuration = unitDuration.value
            ),
            serviceCharges = serviceCharges.value.price,
            emergencyService = emergencyService.value == EmergencyServiceOption.AVAILABLE,
            location = Location(
                lat = serviceLocation.value?.latitude ?: 0.0,
                long = serviceLocation.value?.longitude ?: 0.0
            )
        )

        viewModelScope.launch {
            try {
                _addServiceResponse.value = ApiResponse.Loading
                Log.e(tag, "Request Body : $serviceProfile")
                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.ADD_USER_SERVICE) {
                        setBody(serviceProfile)
                    }

                val result: ResponseWrapper<JsonObject> = response.body()

                if (result.status == true) {
                    _addServiceResponse.value = ApiResponse.Success(result)
                } else {
                    _addServiceResponse.value = ApiResponse.Error(result.message)
                }
            } catch (e: Exception) {
                Log.e(tag, "Exception : ${e.message.toString()}")
                _addServiceResponse.value =
                    ApiResponse.Error("Exception : ${e.message.toString()}")
            }
        }
    }

    fun onCategorySelected(index: Int) {
        val response = _categoryList.value
        if (response is ApiResponse.Success) {
            val currentList = response.data
            _selectedCategory.value = currentList[index]!!
        }
    }

    fun updateSubCategory(index: Int) {
        val selectedCategory = _selectedCategory.value
        val updatedSubCategory = selectedCategory.subCategory.mapIndexed { i, item ->
            val isSelected = i == index
            item.copy(isSelected = isSelected)
        }
        _selectedCategory.value = selectedCategory.copy(subCategory = updatedSubCategory)
    }

    // Api Call
    fun getCategoryList() {
        viewModelScope.launch {
            try {
                _categoryList.value = ApiResponse.Loading
                var response =
                    apiClient.client.get(AppConfig.BASE_URL + ApiEndpoint.CATEGORY_LIST)
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

    fun updateUserProfile() {
        viewModelScope.launch {
            try {

                _updatedUserProfileResponse.value = ApiResponse.Loading

                val response: HttpResponse =
                    apiClient.client.post(AppConfig.BASE_URL + ApiEndpoint.UPDATE_USER_PROFILE) {
                        setBody(mapOf("userName" to fullName.value))
                    }

                val result: ResponseWrapper<UpdateUserProfileResponse> = response.body()

                if (result.status) {

                    if (result.data == null) {
                        _updatedUserProfileResponse.value = ApiResponse.Error("No Data Found")
                        return@launch
                    }
                    _updatedUserProfileResponse.value = ApiResponse.Success(result.data)
                } else {
                    _updatedUserProfileResponse.value = ApiResponse.Error(result.message)
                }

            } catch (exception: Exception) {
                Log.e(tag, exception.message.toString())
                _updatedUserProfileResponse.value =
                    ApiResponse.Error(exception.message.toString())
            }
        }
    }
}

@Serializable
data class ServiceProfile(
    val categoryId: String = "",
    val subCategoryId: String = "",
    val serviceTitle: String = "",
    val serviceDescription: String = "",
    val experienceLevel: String = "",
    val chargeBasis: ChargeBasis? = null,
    val serviceCharges: Int = 0,
    val emergencyService: Boolean = false,
    val location: Location? = null
)

@Serializable
data class ChargeBasis(
    val price: Int = 0,
    val pricingType: String = "",
    val unitDuration: Int = 0
)

@Serializable
data class Location(
    val lat: Double = 0.0,
    val long: Double = 0.0
)