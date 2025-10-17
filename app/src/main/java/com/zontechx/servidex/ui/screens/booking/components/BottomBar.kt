package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun BottomBar(onClick: () -> Unit = {}) {

    val vm: BookingVM = viewModel()
    val serviceDetails by vm.serviceDetails.collectAsState()

    val priceType =
        if (serviceDetails?.chargeBasis?.pricingType == AppConstant.PricingType.MINUTES) {
            "Minutes"
        } else {
            "Hours"
        }

    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppColor.gray_1.copy(alpha = 0.15f), // darker at top
                            Color.White                // fade out to bottom
                        ),
                    )
                )
        )

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .background(color = AppColor.White)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Price",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        fontSize = 16.sp,
                        color = AppColor.gray_4
                    )
                )

                Row(
                    modifier = Modifier.padding(top = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${serviceDetails?.chargeBasis?.price ?: "00.00"}",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            fontSize = 20.sp,
                            color = AppColor.Black
                        )
                    )
                    Text(
                        text = " /${priceType}",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            fontSize = 16.sp,
                            color = AppColor.gray_4
                        )
                    )
                }

            }
            AnimatedButton(
                text = "Book Now",
                onClick = { onClick() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}