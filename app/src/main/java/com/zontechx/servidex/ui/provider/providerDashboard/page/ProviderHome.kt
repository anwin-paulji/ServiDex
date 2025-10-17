package com.zontechx.servidex.ui.provider.providerDashboard.page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.provider.providerDashboard.component.ProviderHomeToolBar
import com.zontechx.servidex.ui.theme.AppColor
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.AnimatedButton

@Composable
fun ProviderHome(pagerState: DrawerState) {

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ProviderHomeToolBar(onProfileClick = {
                scope.launch {
                    pagerState.open()
                }
            })
        }, modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp, end = 16.dp
                )
        ) {
//            BookingStatusCount()
            AddServiceButton()
//            OnGoingServices()
        }
    }
}

@Composable
fun AddServiceButton() {

    val rootNavController = LocalRootNavController.current

    AnimatedButton(
        text = AppConstant.ADD_YOUR_SERVICE,
        backgroundColor = AppColor.White,
        buttonBorderColor = AppColor.Black,
        cornerRadius = 8.dp,
        paddingEnd = 0.dp,
        paddingStart = 0.dp,
        paddingTop = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        textSyle = TextStyle(
            color = AppColor.Black,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.roboto_medium))
        ),
        textPaddingValues = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            bottom = 12.dp,
            top = 12.dp
        ),
        onClick = { rootNavController.navigate(Screen.AddServiceScreen.route) },
    )
}

@Preview
@Composable
fun BookingStatusCount() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        BookingStatus(
            modifier = Modifier.weight(1f),
            title = "Active\nBookings",
            count = 5,
            icon = R.drawable.ic_calender,
            contentColor = AppColor.green_text
        )
        BookingStatus(
            modifier = Modifier.weight(1f),
            title = "Pending\nRequests",
            icon = R.drawable.ic_mail_tick,
            contentColor = AppColor.Primary
        )
        BookingStatus(
            modifier = Modifier.weight(1f),
            title = "Cancelled\nBookings",
            icon = R.drawable.ic_mail_tick,
            contentColor = AppColor.red_text
        )
    }
}

@Preview
@Composable
fun BookingStatus(
    modifier: Modifier = Modifier,
    title: String = "Active\nBookings",
    count: Int = 5,
    icon: Int = R.drawable.ic_calender,
    contentColor: Color = AppColor.Black
) {
    Card(
        modifier = modifier, colors = CardColors(
            containerColor = AppColor.green_1,
            contentColor = AppColor.Black,
            disabledContainerColor = AppColor.White,
            disabledContentColor = AppColor.Black
        ), onClick = { }, elevation = CardDefaults.cardElevation(8.dp) // ✅ proper way
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(2.5f), text = title, style = TextStyle(
                        color = AppColor.Black,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    )
                )
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .weight(1f)
                )
            }
            Text(
                text = "${count}", style = TextStyle(
                    color = contentColor,
                    fontSize = 36.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold))
                )
            )
        }
    }
}

@Composable
fun OnGoingServices() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ongoing Services", style = TextStyle(
                color = AppColor.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp)) // <-- shadow here
                .clip(RoundedCornerShape(20.dp))
                .background(AppColor.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /** Progress
             * CONFIRMED : 0.1f
             * IN_PROGRESS : 0.38f
             * COMPLETED : 0.64f
             * PAYMENT : 1f
            * */

            BufferedLinearProgress(
                progress = 0.64f,
                height = 8.dp
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirmed",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Start
                    )
                )
                Text(
                    text = "In-Progress",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Completed",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Payment",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}
//⦁	PENDING
//⦁	REJECTED
//⦁	CONFIRMED
//⦁	IN_PROGRESS
//⦁	COMPLETED
//⦁	CANCELLED

@Preview
@Composable
fun OnGoingServicesPreview() {
    OnGoingServices()
}


@Composable
fun BufferedLinearProgress(
    progress: Float,      // played 0..1
    modifier: Modifier = Modifier, height: Dp = 6.dp
) {
    val p = progress.coerceIn(0f, 1f)
    val animatedP by animateFloatAsState(p, tween(350))

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val w = size.width
        val h = size.height

        // track
        drawRoundRect(
            color = Color(0xFFECECEC), size = size, cornerRadius = CornerRadius(h / 2f, h / 2f)
        )

        // played (front)
        drawRoundRect(
            color = AppColor.Primary,
            topLeft = Offset(0f, 0f),
            size = Size(width = w * animatedP, height = h),
            cornerRadius = CornerRadius(h / 2f, h / 2f)
        )
    }
}
