package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun TopBar(
modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {}) {

    val titleStyle = TextStyle(color = AppColor.Black, fontSize = 16.sp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
        .background(color = AppColor.White)
        .fillMaxWidth()
        .height(48.dp)
        .padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier.clickable { onClickBack() }.padding(start = 16.dp, end = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                modifier = Modifier
                    .rotate(90f)
                    .size(34.dp),
                contentDescription = ""
            )
        }

        Text(
            text = "Booking",
            style = titleStyle)
    }
}