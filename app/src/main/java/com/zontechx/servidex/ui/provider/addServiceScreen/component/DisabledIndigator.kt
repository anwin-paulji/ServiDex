package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun DisabledIndigator(index: Int = 0) {

    var _modifier = Modifier
        .padding(horizontal = 2.dp)
        .size(30.dp)
        .clip(shape = RoundedCornerShape(12.dp))
        .background(color = AppColor.Yellow)

    Box(
        modifier = _modifier
    ) {
        Text(
            text = index.toString(),
            style = TextStyle(textAlign = TextAlign.Center),
            textAlign = TextAlign.Center,
            modifier = _modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        )
    }
}