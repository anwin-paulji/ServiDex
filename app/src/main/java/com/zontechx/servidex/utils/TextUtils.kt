package com.zontechx.servidex.utils

data class ValidationState(
    var Valid: Boolean = false,
    var message: String? = null
)

object TextUtils {

    private fun validateEmail(email: String): ValidationState {
        return if (email.isBlank()) ValidationState(false, "Email cannot be empty")
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ValidationState(false, "Email cannot be empty")
        else ValidationState(true, "Valid Email")
    }
}