package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.theme.AppColor
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM

@Preview
@Composable
fun ServiceImage(vm: AddServiceScreenVM = viewModel()) {

    val TAG = "ServiceImage"

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W500, fontSize = 20.sp)

    var titleModifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    LaunchedEffect(Unit) {
        Log.e(TAG, "Launched : ServiceImage")
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(bottom = 16.dp)
    ) {
        Text(text = AppConstant.UPLOAD_SERVICE_IMAGE, style = titleTextStyle, modifier = titleModifier)
        Text(text = AppConstant.SERVICE_IMAGE_DESCRIPTION, style = descritptionTextStyle, modifier = titleModifier)

        DottedBorderBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp),
            cornerRadius = 10.dp,
            content = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(painter = painterResource(R.drawable.image_24), contentDescription = null)
                        Text(text = AppConstant.ADD_IMAGE, style = titleTextStyle, modifier = titleModifier)
                    }
            }
        )
    }
}

@Composable
fun DottedBorderBox(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    strokeWidth: Float = 4f,
    dotSpacing: Float = 10f,
    cornerRadius: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                val paint = Paint().asFrameworkPaint().apply {
                    this.color = color.toArgb()
                    this.isAntiAlias = true
                    this.style = android.graphics.Paint.Style.STROKE
                    this.strokeWidth = strokeWidth
                    this.pathEffect = android.graphics.DashPathEffect(floatArrayOf(strokeWidth, dotSpacing), 0f)
                }

                drawIntoCanvas {
                    it.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        cornerRadius.toPx(),
                        cornerRadius.toPx(),
                        paint
                    )
                }
            }
    ) {
        content()
    }
}