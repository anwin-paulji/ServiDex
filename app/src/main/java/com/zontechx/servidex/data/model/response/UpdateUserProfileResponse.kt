package com.zontechx.servidex.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileResponse(
    val _id: String,
    val mobileNumber: String,
    val userName: String,
    val email: String,
    val isProfileCompleted: Boolean,
    val isBusinessEnabled: Boolean,
    val updatedAt: Long,
    val defaultProviderType: String,
    val subScriberId: String,
    val createdAt: Long
)