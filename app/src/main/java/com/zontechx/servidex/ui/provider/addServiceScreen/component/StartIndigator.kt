package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.theme.AppColor


@Composable
fun StartIndigator(filled: Boolean = false, index: Int = 0) {

    var _modifier = Modifier
        .padding(horizontal = 2.dp)
        .size(30.dp)
        .clip(shape = RoundedCornerShape(12.dp))
        .background(color = AppColor.Yellow)

    var titleTextStyle =
        TextStyle(fontWeight = FontWeight.W400, color = AppColor.Black, fontSize = 18.sp)

    var title = when (index) {
        0 -> AppConstant.STEP_1
        1 -> AppConstant.STEP_2
        2 -> AppConstant.STEP_3
//        3 -> AppConstant.STEP_4
        else -> AppConstant.STEP_1
    }

    if (filled) {
        Image(
            painter = painterResource(R.drawable.tick),
            contentDescription = null,
            modifier = _modifier
        )
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = (index + 1).toString(),
                style = TextStyle(textAlign = TextAlign.Center),
                textAlign = TextAlign.Center,
                modifier = _modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            )
            Text(text = title, style = titleTextStyle)
        }
    }
}

