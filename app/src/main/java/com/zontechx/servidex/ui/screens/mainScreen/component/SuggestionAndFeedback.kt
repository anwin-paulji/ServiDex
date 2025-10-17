package com.zontechx.servidex.ui.screens.mainScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun SuggestionAndFeedback(onClick: () -> Unit = {}){

    var rootContainer_Modifier = Modifier
        .fillMaxWidth()
        .background(color = AppColor.white_1)
        .padding(end = 15.dp, start = 15.dp)

    var title_TextStyle =
        TextStyle(color = AppColor.Black, fontSize = 18.sp, fontWeight = FontWeight.W800)
    var content_TextStyle =
        TextStyle(color = AppColor.darkGray, fontSize = 12.sp, fontWeight = FontWeight.W400)
    var button_TextStyle =
        TextStyle(color = AppColor.darkGray, fontSize = 12.sp, fontWeight = FontWeight.W400)

    var contentText_Modifier = Modifier.padding(top = 10.dp)

    var image_Modifier = Modifier
        .width(200.dp)
        .height(170.dp)

    Row(
        modifier = rootContainer_Modifier
    ) {
        Image(
            painter = painterResource(R.drawable.feedback),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = image_Modifier
                .background(color = AppColor.white_1)
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 15.dp)
                .wrapContentHeight(Alignment.Bottom)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Help Us Improve", style = title_TextStyle)
                Text(
                    modifier = contentText_Modifier,
                    text = "We’d love to hear your feedback! Whether it's a feature idea or something that’s not working, let us know.",
                    style = content_TextStyle
                )

                AnimatedButton(
                    text = "Feedback & Suggestions",
                    textSize = 14.sp,
                    textSyle = TextStyle(textAlign = TextAlign.Center),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    backgroundColor = AppColor.White,
                    buttonBorderColor = AppColor.gray_1,
                    cornerRadius = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textColor = AppColor.darkGray,
                    paddingTop = 0.dp,
                    paddingBottom = 0.dp,
                    paddingStart = 0.dp,
                    paddingEnd = 0.dp,
                    onClick = { onClick() },
                )
            }
        }
    }
}

@Preview
@Composable
fun SuggestionAndFeedbackPreview() {
    SuggestionAndFeedback()
}