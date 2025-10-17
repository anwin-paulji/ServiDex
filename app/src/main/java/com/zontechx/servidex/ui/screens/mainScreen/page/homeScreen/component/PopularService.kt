package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun  PopularService(onClickViewAll: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Popular Service",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W700)
            )
            Text(
                text = "View all",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = AppColor.Primary
                ),
                modifier = Modifier.clickable {
                    onClickViewAll()
                }
            )
        }

        PopularServiceList()
    }
}

@Composable
fun PopularServiceList() {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            PopularServiceItem()
        }
    }
}

@Composable
fun PopularServiceItem() {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = AppColor.Primary)

    ) {
        Image(
            painter = painterResource(R.drawable.on1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(3.dp)
        )
    }
}
