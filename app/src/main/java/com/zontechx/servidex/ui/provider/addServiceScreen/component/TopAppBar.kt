package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun TopAppBar(filledIndex: Int = 0) {

    var enableIndex = filledIndex + 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = AppColor.white_2)
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Row {
                when (filledIndex) {
                    1 -> {
                        StartIndigator(filled = true, index = filledIndex)
                    }

                    2 -> {
                        StartIndigator(filled = true, index = filledIndex)
                        StartIndigator(filled = true, index = filledIndex)
                    }

                    3 -> {
                        StartIndigator(filled = true, index = filledIndex)
                        StartIndigator(filled = true, index = filledIndex)
                        StartIndigator(filled = true, index = filledIndex)
                    }

                    else -> {}
                }

                when (enableIndex) {
                    1 -> {
                        StartIndigator(filled = false, index = filledIndex)
                    }

                    2 -> {
                        StartIndigator(filled = false, index = filledIndex)
                    }

                    3 -> {
                        StartIndigator(filled = false, index = filledIndex)
                    }

                    else -> {}
                }
            }

            Row {
                when (filledIndex) {
                    0 -> {
                        DisabledIndigator(index = 2)
                        DisabledIndigator(index = 3)
                    }

                    1 -> {
                        DisabledIndigator(index = 3)
                    }

                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    TopAppBar(0)
}
