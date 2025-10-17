package com.zontechx.servidex.ui.provider.providerDashboard.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun ProviderChat() {
    Scaffold(
        modifier = Modifier.fillMaxWidth().background(AppColor.White)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(innerPadding)
                .background(AppColor.gray_3)
        ) {

        }
    }
}