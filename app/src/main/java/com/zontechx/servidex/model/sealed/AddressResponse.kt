package com.zontechx.servidex.model.sealed

import com.zontechx.servidex.utils.CurrentAddress

sealed class AddressResponse() {
    data class Success(val address: CurrentAddress) : AddressResponse()
    data class Error(val message: String) : AddressResponse()
}