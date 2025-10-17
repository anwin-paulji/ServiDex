package com.zontechx.servidex.data.static

object AppConstant {

    val ON_BOARDING_TITLE_1 = "Find Reliable Services";
    val ON_BOARDING_TITLE_2 = "Book With Ease";
    val ON_BOARDING_TITLE_3 = "Pay Securely";
    val ON_BOARDING_1 =
        "Discover trusted professionals for all your home service needs. Whether it’s cleaning, repairs, or maintenance, we’ve got you covered!";
    val ON_BOARDING_2 =
        "Scheduling a service is just a few taps away. Select your preferred time and let us handle the rest.";
    val ON_BOARDING_3 =
        "Enjoy hassle-free payments with our secure platform. You only pay once the job is done to your satisfaction.";
    val LOGIN_REGISTER = "Login or SignUp";
    val SKIP = "Skip";
    val LOGIN_REGISTER_QUOT = "Quality service at your fingertips";
    val GET_OTP = "Get OTP";
    val ENABLE_LOCATION = "Enable Location";
    val COUNTRY_CODE = "+91 | "
    val LOGIN_PLACEHOLDER = "Enter your Mobile Number"
    val CUSTOMER_NAME = "Customer Name"
    val MOBILE_NUMBER = "Mobile Number"
    val OTP_PLACEHOLDER = "Enter OTP"
    val VERIFY_OTP = "Verify OTP"
    val ENTER_CODE = "Enter Code";
    val CONTENT_1 =
        "Join a network of reliable services with a simple and secure sign-up. Just enter your mobile number to get started"
    val CONTENT_2 =
        "For your security, we've sent an OTP to your mobile number. Enter the code to verify and continue.";
    val CONTENT_3 = "We Sent a verification code to your mobile number";
    val DONE = "Done";

    var FULL_NAME = "Enter you Full Name"
    var FULL_NAME_HINT = "Full Name"
    var FULL_NAME_DESCRIPTION = "Please enter your full legal name as it appears on your ID"
    var EMAIL_DESCRIPTION =
        "Add your email to receive important updates, bookings, and support info."
    var EMAIL = "Enter your Email"
    var EMAIL_HINT = "Email (Optional)"

//    var STEP_1 = "Basic details"
//    var STEP_2 = "Service Details"
//    var STEP_3 = "Service Image"
//    var STEP_4 = "Legal & Agreement"

    var STEP_1 = "Service Details"
    var STEP_2 = "Service Image"
    var STEP_3 = "Legal & Agreement"

    var SERVICE_TITLE = "Enter Service Title"
    var SERVICE_TITLE_HINT = "Service Title"
    var SERVICE_TITLE_DESCRIPTION = "Please enter your full legal name as it appears on your ID"

    var SERVICE_DESCRIPTION = "Enter Service Description"
    var SERVICE_DESCRIPTION_HINT = "Service Description"
    var SERVICE_DESCRIPTION_DESCRIPTION =
        "Please enter your full legal name as it appears on your ID"

    var SELECT_CATEGORY = "Select Category"
    var SELECT_SUB_CATEGORY = "Select Sub-Category"
    var CATEGORY_DESCRIPTION = "Please select your category"

    var SET_SERVICE_AREA = "Set Your Service Area"

    var YES = "Yes"
    var NO = "No"
    var ADD = "Add"

    var UPLOAD_SERVICE_IMAGE = "Upload Service Image"
    var UPLOAD_IMAGE = "Upload Image"
    var SERVICE_IMAGE_DESCRIPTION = "Upload clear photos (max 5) to help customers see your service"
    var ADD_IMAGE = "Add Image"
    var ENABLE_LOCAION_DESCRIPTION =
        "Enable location access to find nearby trusted service professionals."
    var CANCEL = "Cancel"
    var APPLY = "Apply"
    val USE_CHOOSE_LOCATION = "Use Current Location"
    val ADD_NEW_LOCATION = "Add a new Location"

    val EXPERIANCE_LEVEL = "Experiance Level"
    val EXPERIANCE_LEVEL_HINT = "Select your experiance level"
    val EXPERIANCE_LEVEL_DESCRIPTION = "Select your experiance level"

    val SERVICE_CHARGES = "Service Charges"
    val SERVICE_CHARGES_DESCRIPTION = ""

    val EMERGENCY_SERVICE = "Available for emergency service"
    val EMERGENCY_SERVICE_DESCRIPTION =
        "Customers can contact you for urgent help, even without booking earlier."

    val SELECTED_LOCATION = "Selected Location"
    val SELECT_TIME_SLOT = "Select Time Slot"
    val SERVICE_ADDERSS = "Service Address"

    val SELECT_DATE = "Select Booking Date"
    val CONTACT_DETAILS = "Contact Details"
    val CONTACT_PERSON_NAME = "Contact Person Name"
    val CONTACT_PERSON_NAME_HINT = "Enter contact person name"
    val CONTACT_PERSON_NUMBER = "Contact Person Number"
    val CONTACT_PERSON_NUMBER_HINT = "Enter contact person number"
    val CONFIRM_BOOKING = "Confirm Booking"

    val ADDITIONAL_NOTES = "Additional Notes"
    val ADDITIONAL_NOTES_HINT = "Add special instructions or requests here..."
    val ADDITIONAL_NOTES_DESCRIPTION =
        "Add any special instructions or requests for the service provider."
    val SWITCH_TO_BUSINESS = "Switch to Business Account"
    val SWITCH_TO_HOME = "Switch to Home"
    val CREATE_YOUR_SERVICE = "Create Your Service"
    val ADD_YOUR_SERVICE = "Add Your Service"
    val SELECT_COVER_IMAGE = "Select Cover Image"
    val CLOSE = "Close"
    val BOOKING_DETAILS = "Booking Details"
    val SERVICE_ADDED_SUCCESSFULLY = "Service Added successfully"
    val SERVICE_UPDATED_SUCCESSFULLY = "Service updated successfully"
    val SERVICE_DELETED_SUCCESSFULLY = "Service deleted successfully"
    val SERVICE_ADDED_FAILED = "Service added failed"
    val SERVICE_UPDATED_FAILED = "Service updated failed"
    val SERVICE_DELETED_FAILED = "Service deleted failed"
    val SERVICE_ADDED_SUCCESSFULLY_DESCRIPTION =
        "Your service has been added successfully, ServiDex verification is in progress. Will receive update in 24 hours"
    val SERVICE_UPDATED_FAILED_DESCRIPTION = "Something went wrong while updating service"
    val USE_CURRENT_LOCATION = "Use Current Location"
    val FILTER = "Filter"
    val SORT_BY = "Sort By"
    val BOOK_NOW = "Book Now"
    val ENABLE_NOTIFICATION = "Enable Notification"
    val NOTIFICATION_DESCRIPTION =
        "Turn on notifications to get instant updates about your bookings and payments."
    val LOGOUT = "Logout"
    val CUSTOMER_DETAILS = "Customer Details"
    val PAYMENT_DETAILS = "Payment Details"
    val SERVICE_DETAILS = "Service Details"
    val BOOKING_SUMMARY = "Booking Summary"

    object ScreenSource {
        const val FROM_MAIN_HOME = "from_main_home"
        const val FROM_MAIN_CATEGORY = "from_main_category"

        const val FROM_DEEP_LINK = "from_deep_link"
        const val FROM_NOTIFICATION = "from_notification"
        const val FROM_CATEGORY = "from_category"
        const val FROM_SERVICE_DETAIL = "from_service_detail"
        const val FROM_ADD_SERVICE = "from_add_service"
        const val FROM_RESPECTIVE_SERVICE = "from_respective_service"
        const val FROM_NEAR_BY_SERVICE_SCREEN = "from_near_by_service_screen"
    }

    object LocalStorageKey {
        const val PERMISSION_DENIED_COUNT = "permission_denied_count"
    }

    object PricingType {
        const val MINUTES = "MINUTES"
        const val HOURS = "HOURS"
    }

    object ScreenName {
        const val HOME_SCREEN = "HOME_SCREEN"
        const val PORVIDER_HOME_SCREEN = "PROVIDER_HOME_SCREEN"
    }
}