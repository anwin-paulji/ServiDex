package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun BottomSubmitButton(enableButton: Boolean, onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = AppColor.Yellow),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 15.dp),
        enabled = enableButton,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
            )
            .clip(RoundedCornerShape(50.dp)),
        onClick = {
            onClick()
        }) {
        Text(text = AppConstant.DONE)
    }
}