package com.zontechx.servidex.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RatingReviewResponse(
    val reviews: List<RatingReview>,
)

@Serializable
data class RatingReview(
    val _id: String,
    val bookingId: String,
    val review: String? = null,
    val rating: Int,
    val userType: String,                // e.g., "SEEKER" or "PROVIDER"
    val status: Boolean = true,
    val ratedBy: String,
    val serviceId: String,
    val customerId: String,
    val serviceProviderId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val ratingFor: String                // e.g., "SERVICE" or "PROVIDER"
)

