package com.zontechx.servidex.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

object GlobalViewModelProvider {
    private val viewModelStore = ViewModelStore()
    private val viewModelFactory = ViewModelProvider.NewInstanceFactory()

    fun <T: ViewModel> get(modelClass: Class<T>):T {
        return ViewModelProvider(viewModelStore, viewModelFactory)[modelClass]
    }
    fun clear() {
        viewModelStore.clear()
    }
}