package com.zontechx.servidex.utils

import java.util.Calendar

object TimeUtils {
    fun formatMillisToDate(millis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are 0-based
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }
}