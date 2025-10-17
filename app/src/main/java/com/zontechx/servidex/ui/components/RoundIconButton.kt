package com.zontechx.servidex.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun RoundIconButton(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppColor.White,
    iconTint: Color = AppColor.Black,
    iconSize: Dp = 24.dp,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(43.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            colorFilter = ColorFilter.tint(iconTint)
        )
    }
}