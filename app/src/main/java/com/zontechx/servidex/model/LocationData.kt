package com.zontechx.servidex.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(var locationName: String = "", var latitude: Double, var longitude: Double)
