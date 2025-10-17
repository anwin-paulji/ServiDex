package com.zontechx.servidex.ui.screens.mainScreen.page.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun ChatScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(innerPadding)
        ) {
            Toolbar()
        }
    }
}



@Preview
@Composable
fun Toolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
    ) {
        Text(
            text = "Chat", modifier = Modifier.padding(16.dp),
            style = TextStyle(
                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )
    }
}