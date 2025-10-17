package com.zontechx.servidex.ui.provider.UploadServiceImage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedComponent(
                onClick = { onBackClick() },
                content = {
                    RoundIconButton(
                        backgroundColor = AppColor.Primary,
                        iconTint = AppColor.Black,
                        iconRes = R.drawable.arrow_left,
                    )
                }
            )


            Text(
                text = AppConstant.UPLOAD_IMAGE, style = TextStyle(
                    color = AppColor.Black, fontSize = 18.sp, fontFamily = FontFamily(
                        Font(R.font.roboto_semibold)
                    )
                )
            )
        }

        Image(painter = painterResource(R.drawable.app_logo_main), contentDescription = "")
    }
}