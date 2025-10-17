package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun OfferBanner() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Your Offers",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W700)
            )
            Text(
                text = "View all",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = AppColor.darkGray
                ),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .clickable{}
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                OfferBannerItem(index = index)
            }
        }
    }




}

@Composable
fun OfferBannerItem(index:Int) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(
                color = if (index % 2 == 0) AppColor.Primary else AppColor.yellow_1
            )
    ) {

    }
}