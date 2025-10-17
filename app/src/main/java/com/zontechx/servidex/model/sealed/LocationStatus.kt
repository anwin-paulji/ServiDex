package com.zontechx.servidex.model.sealed

sealed class LocationStatus {
    data class Success(val latitude: Double, val longitude: Double) : LocationStatus()
    data class Error(val message: String) : LocationStatus()
}