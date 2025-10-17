package com.zontechx.servidex.model.enum

enum class BookingStatus(val value: String) {
    ALL(""),
    PENDING("PENDING"),
    REJECTED("REJECTED"),
    CONFIRMED("CONFIRMED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
}