package com.zontechx.servidex.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun AnimatedButton(
    textColor: Color = AppColor.buttonTestColor,
    backgroundColor: Color = AppColor.Primary,
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    cornerRadius: Dp = 50.dp,
    buttonBorderColor: Color = AppColor.Primary,
    textPaddingValues: PaddingValues = PaddingValues(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 15.dp),
    paddingStart: Dp = 20.dp,
    paddingEnd: Dp = 20.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    fontFamily: FontFamily = FontFamily(Font(R.font.roboto_semibold)),
    textSize: TextUnit = 16.sp,
    textSyle: TextStyle = TextStyle(fontSize = textSize, fontFamily = fontFamily, color = textColor),
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
                bottom = paddingBottom
            )
            .clip(RoundedCornerShape(cornerRadius))
            .border(width = 0.5.dp, color = buttonBorderColor, shape = RoundedCornerShape(cornerRadius))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        try {
                            val released = tryAwaitRelease()
                            if (released) onClick()
                        } finally {
                            isPressed = false
                        }
                    })
            }
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textSyle,
            modifier = Modifier.padding(textPaddingValues),
        )
    }
}