package com.zontechx.servidex.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    val token: String,
    val mobileNumber: String,
    val userData: UserData
)


@Serializable
data class UserData(
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

//{
//        "_id" : "689e34202f4bc0021deef2a0",
//        "mobileNumber" : "7708774542",
//        "userName" : "Anwin Paulji",
//        "email" : "",
//        "isProfileCompleted" : true,
//        "isBusinessEnabled" : false,
//        "fcmToken" : "dWO2OVoXRi2ITWXbtxQqnT:APA91bEeFq7Ec5f3E4pTrUF-NEGV2amvFdIxRjkxWIxSwcYF3lYwdZD2ZVSMJ8tvAi3HizaALRRTz9BGxBmSwWpm6bu9Yu3CuOes1zpRaqdg3X2ve40dfDw",
//        "updatedAt" : 1755198519516,
//        "defaultProviderType" : "INDIVIDUAL",
//        "subScriberId" : "",
//        "createdAt" : 1755198496400
//      }

        // {
//        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGVfbnVtYmVyIjoiNzcwODc3NDU0MiIsImlhdCI6MTc1MzYxNDA5MiwiZXhwIjoxNzUzNzAwNDkyfQ.bH_wpCE09ikw5uxSdr_LohN_w3-KcG8G0_rKVr3NNe8",
//        "mobileNumber": "7708774542",
//        "userData": {
//            "_id": "687fcce9e8d4a737bee45b41",
//            "mobileNumber": "7708774542",
//            "userName": "",
//            "email": "",
//            "isProfileCompleted": false,
//            "isBusinessEnabled": false,
//            "updatedAt": 1753205993096,
//            "defaultProviderType": "INDIVIDUAL",
//            "subScriberId": "",
//            "createdAt": 1753205993096
//        }
//    }