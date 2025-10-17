package com.zontechx.servidex.ui.screens.notificationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zontechx.servidex.ui.screens.notificationScreen.component.ToolBar
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun NotificationScreen(rootNavController: NavHostController) {
    Scaffold { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppColor.White)
                    .padding(innerPadding)
            ) {
                ToolBar()
            }
        }
    }
}
