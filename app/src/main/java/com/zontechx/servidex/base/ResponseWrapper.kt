package com.zontechx.servidex.base

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)


@Serializable
data class RequestData(val requestId: String? = null, val mobileNumber: String? = null) {
    override fun toString(): String {
        return "RequestData(requestId='$requestId')"
    }
}


@Serializable
data class LoginRegisterResponse(
    val verificationId: String = "",
    val mobileNumber: String = "",
    val responseCode: String = "",
    val timeout: String = "",
)


// "verificationId": "800614",
//        "mobileNumber": "7708774542",
//        "responseCode": "200",
//        "timeout": "60.0",
