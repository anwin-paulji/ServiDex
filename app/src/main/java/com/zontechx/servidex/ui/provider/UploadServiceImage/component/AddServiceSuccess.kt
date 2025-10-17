package com.zontechx.servidex.ui.provider.UploadServiceImage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun AddServiceSuccess(onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.success)
        )

        val progress by animateLottieCompositionAsState(
            composition,
            iterations = 1
        )
        Spacer(modifier = Modifier.weight(1f, fill = true))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .weight(1f, fill = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = AppConstant.SERVICE_ADDED_SUCCESSFULLY,
                color = AppColor.Black,
                modifier = Modifier.padding(bottom = 10.dp),
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = AppConstant.SERVICE_ADDED_SUCCESSFULLY_DESCRIPTION,
                color = AppColor.gray_4,
                minLines = 3,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    textAlign = TextAlign.Center
                )
            )
        }

        LottieAnimation(
            modifier = Modifier
                .size(300.dp),
            composition = composition,
            progress = { progress }
        )

        Spacer(modifier = Modifier.weight(1f, fill = true))

        Column {
            AnimatedButton(
                text = AppConstant.BOOKING_DETAILS,
                modifier = Modifier.fillMaxWidth(),
                paddingBottom = 20.dp,
                onClick = { onClick() },
                textPaddingValues = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 15.dp,
                    top = 15.dp
                )
            )
            AnimatedButton(
                text = AppConstant.CLOSE,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = AppColor.White,
                textColor = AppColor.Primary,
                buttonBorderColor = AppColor.White,
                onClick = { onClick() },
                textPaddingValues = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 15.dp,
                    top = 15.dp
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f, fill = true))
    }
}