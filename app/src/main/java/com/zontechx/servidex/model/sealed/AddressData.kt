package com.zontechx.servidex.model.sealed

import com.zontechx.servidex.model.LocationData


sealed class AddressData {
    data class Success(val address: LocationData) : AddressData()//CurrentAddress
    data class Failure(val message: String) : AddressData()
}