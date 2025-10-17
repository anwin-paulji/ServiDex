package com.zontechx.servidex.data.model.response

import com.zontechx.servidex.model.response.ServiceDetailsResponse
import kotlinx.serialization.Serializable

@Serializable
data class HomeScreenResponse(
	val userData: MyUserData? = null,
    val topCategoryData: TopCategoryData? = null,
    val nearByService: NearByServiceData? = null,
    val featureService: FeatureService? = null
)

@Serializable
data class NearByServiceData(
    val data: List<ServiceDetailsResponse> = emptyList(),
    val message: String? = null,
    val status: Boolean? = null
)

@Serializable
data class Location(
    val lat: Double = 0.0,
    val jsonMemberLong: Double = 0.0
)

@Serializable
data class TopCategoryData(
    val message: String? = null,
    val status: Boolean? = null
)

@Serializable
data class FeatureService(
    val message: String? = null,
    val status: Boolean? = null,
    val data: List<VersionItem> = emptyList()
)

@Serializable
data class MyUserData(
    val message: String? = null,
    val status: Boolean? = null,
    val data: UserData? = null
)

@Serializable
data class VersionItem(
    val versionName: String? = null,
    val versionCode: Int? = null,
    val minSupportedVersionCode: Int = 0,
    val note: List<String> = emptyList(),
    val isForceUpdate: Boolean? = null,
    val platform: String? = null,
    val createdBy: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)


//@Serializable
//data class UserData(
//	val message: String? = null,
//	val status: Boolean? = null
//)

//"featureService" : {
//                       "status" : true,
//                       "message" : "Versions Retrieved Successfully",
//                       "data" : [ {
//                         "_id" : "68ba8c0e56138c505f3f6cfd",
//                         "versionName" : "1.0.0",
//                         "versionCode" : 1,
//                         "minSupportedVersionCode" : 1,
//                         "note" : [ "Initial launch of Servidex app", "Post your own services or list your business", "Search and book trusted service providers", "Basic booking flow with time slot selection", "Secure login with OTP verification" ],
//                         "isForceUpdate" : false,
//                         "platform" : "android",
//                         "createdBy" : "admin",
//                         "createdAt" : 1757056014005,
//                         "updatedAt" : 1757056014005
//                       }, {
//                         "_id" : "68ba942a301b771eba0a84bb",
//                         "versionName" : "1.0.1",
//                         "versionCode" : 2,
//                         "minSupportedVersionCode" : 1,
//                         "note" : [ "Initial launch of Servidex app", "Post your own services or list your business", "Search and book trusted service providers", "Basic booking flow with time slot selection", "Secure login with OTP verification" ],
//                         "isForceUpdate" : false,
//                         "platform" : "android",
//                         "createdBy" : "admin",
//                         "createdAt" : 1757058090236,
//                         "updatedAt" : 1757058090236
//                       } ]
//                     },
//                     "nearByService" : {
//                       "status" : true,
//                       "message" : "Success",
//                       "data" : [ {
//                         "_id" : "68a366e5ea5f952dc7bab527",
//                         "categoryId" : "68700517fe8e7763abcb5387",
//                         "subCategoryId" : "5d14f5ae-4a34-4660-9f89-23e27122274e",
//                         "serviceTitle" : "Anwin Exterior Works",
//                         "serviceDescription" : "Only For Customer ",
//                         "experienceLevel" : "2-3 years",
//                         "chargeBasis" : {
//                           "price" : 300,
//                           "pricingType" : "HOURS",
//                           "unitDuration" : 30
//                         },
//                         "location" : {
//                           "lat" : 12.930973300000002,
//                           "long" : 80.235889
//                         },
//                         "servideProviderId" : "68a0da6095a5bb68d780afab"
//                       }, {
//                         "_id" : "68ac386b38829a0d6c6f6d12",
//                         "categoryId" : "68700443fe8e7763abcb5385",
//                         "subCategoryId" : "9bb1d603-99f2-40be-b5e1-2acb86bf77c9",
//                         "serviceTitle" : "Demo testing",
//                         "serviceDescription" : "test",
//                         "experienceLevel" : "5-6 years",
//                         "chargeBasis" : {
//                           "price" : 300,
//                           "pricingType" : "HOURS",
//                           "unitDuration" : 30
//                         },
//                         "location" : {
//                           "lat" : 12.919984,
//                           "long" : 80.2279439
//                         },
//                         "servideProviderId" : "689e34202f4bc0021deef2a0"
//                       } ]