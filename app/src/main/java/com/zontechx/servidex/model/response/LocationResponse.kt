package com.zontechx.servidex.model.response

import kotlinx.serialization.Serializable

data class LocationResponse(
    val status: Boolean,
    val message: String,
    val data: List<LocationDataItem>
)

@Serializable
data class LocationDataItem(
    val _id: MongoId,

//    val country: String,
//    val district: String,
    val locationName: String,
    val state: String,
    val location: GeoLocation
)

@Serializable
data class MongoId(
    val `$oid`: String
)

@Serializable
data class GeoLocation(
    val type: String,
    val coordinates: List<Double> // [longitude, latitude]
)
