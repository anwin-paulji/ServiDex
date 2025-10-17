package com.zontechx.servidex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun CustomTimePicker(
    title: String = "Select Time",
    time: String = "00:00 AM",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
        .clip(shape = RoundedCornerShape(8.dp))
        .border(width = 0.1.dp, color = AppColor.gray_4, shape = RoundedCornerShape(8.dp))
        .background(Color.White)
) {

    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.time_icon_48),
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(AppColor.gray_4),
                contentDescription = "Time Picker Icon"
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        color = AppColor.Primary,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = time,
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontWeight = FontWeight.W400,
                        fontSize = 18.sp
                    )
                )
            }
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(AppColor.gray_4),
                contentDescription = "Time Picker Icon"
            )
        }

    }
}