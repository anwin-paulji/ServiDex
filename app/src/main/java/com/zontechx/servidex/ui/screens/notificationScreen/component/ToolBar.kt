package com.zontechx.servidex.ui.screens.notificationScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun ToolBar() {

    val rootNavController = LocalRootNavController.current

    fun handleBackPress() {
        rootNavController.popBackStack()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundIconButton(
            iconRes = R.drawable.arrow_left,
            modifier = Modifier.padding(start = 16.dp).clickable { handleBackPress() },
            backgroundColor = Color.Transparent,
            iconTint = Color.Black
        )

        Text(
            text = "Notifications", style = TextStyle(
                fontSize = 20.sp, color = AppColor.Black, fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )
    }
}