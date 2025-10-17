package com.zontechx.servidex.ui.screens.mainScreen.page.profileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize().padding().background(AppColor.Primary)
    ) {
        Text(text = "Home Page")
    }
}