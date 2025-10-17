package com.zontechx.servidex.model

import com.zontechx.servidex.model.enum.BookingStatus

data class ConfirmationSheetState(
    val title: String = "",
    val description: String = "",
    val positiveText: String = "",
    val negativeText: String? = null,
    val visible: Boolean = false,
    val newBookingStatus: BookingStatus = BookingStatus.ALL
)

