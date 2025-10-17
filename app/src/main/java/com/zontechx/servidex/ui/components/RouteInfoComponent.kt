package com.zontechx.servidex.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.model.response.RouteInfo
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun RouteInfoComponent(routeInfoData: RouteInfo) {

    val travelTime = routeInfoData.travelTime
    val time = when {
        travelTime.hours == 0 -> "${travelTime.minutes}m"
        else -> "${travelTime.hours}h ${travelTime.minutes}m"
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_fill),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = AppColor.Primary
            )
            Text(
                text = "${routeInfoData.distance} km",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    color = AppColor.gray_4
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_watch_fill),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = AppColor.Primary
            )
            Text(
                text = time,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    color = AppColor.gray_4
                )
            )
        }
    }
}