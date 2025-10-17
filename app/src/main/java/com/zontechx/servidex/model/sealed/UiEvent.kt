package com.zontechx.servidex.model.sealed

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    data class PopUpTo(val route: String, val inclusive: Boolean = false) : UiEvent()
    object PopBackStack : UiEvent()
}