package com.zontechx.servidex.ui.vm

import androidx.lifecycle.ViewModel
import com.zontechx.servidex.repo.UserRepository

class CommonVM(private val userRepository: UserRepository): ViewModel() {

    suspend fun doSomething() {
        userRepository.saveUserName("Anwin");
    }
}