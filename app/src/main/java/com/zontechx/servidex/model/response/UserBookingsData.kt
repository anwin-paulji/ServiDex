package com.zontechx.servidex.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserBookingsData(
    val bookings: List<Bookings>,
)

@Serializable
data class Bookings(
    val _id: String,
    val customerId: String,
    val serviceProviderId: String,
    val serviceId: String,
    val serviceDate: String,
    val paymentStatus: String,
    val bookingStatus: String,
    val bookingStatusHistory: List<BookingStatusHistory>,
    val paymentStatusHistory: List<PaymentStatusHistory>,
    val bookingTime: BookingTime,
    val address: Address,
    val contactDetails: ContactDetails,
    val specialInstructions: String,
    val currency: String,
    val createdAt: Long,
    val updatedAt: Long,
    val serviceDetails: ServiceDetails,
    val chargeBasis: BookedChargeBasis? = null,
    val routeInfo: RouteInfo? = null,
    val isServiceRatingCompleted: Boolean? = false,
    val isCustomerRatingCompleted: Boolean? = false
)

@Serializable
data class BookingStatusHistory(
    val bookingStatus: String,
    val timestamp: Long
)

@Serializable
data class PaymentStatusHistory(
    val paymentStatus: String,
    val timestamp: Long
)

@Serializable
data class BookingTime(
    val startTime: String?, // can be null
    val endTime: String?    // can be null
)

@Serializable
data class Address(
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class ContactDetails(
    val name: String,
    val mobileNumber: String
)

@Serializable
data class ServiceDetails(
    val serviceName: String,
    val category: CategoryBooked,
    val subCategory: SubCategoryBooked,
    val serviceImages: List<BookedServiceImage>
)

@Serializable
data class CategoryBooked(
    val categoryName: String,
    val categoryIcon: String,
    val categoryId: String
)

@Serializable
data class SubCategoryBooked(
    val subCategoryName: String,
    val subCategoryId: String
)

@Serializable
data class BookedServiceImage(
    val originalUrl: String,
    val croppedUrl: String,
    val isPrimary: Boolean
)

@Serializable
data class BookedChargeBasis(
    val price: Int,
    val pricingType: String,
    val unitDuration: Int
)

@Serializable
data class RouteInfo(
    val distance: String,
    val travelTime: TravelTime
)

@Serializable
data class TravelTime(
    val hours: Int,
    val minutes: Int
)