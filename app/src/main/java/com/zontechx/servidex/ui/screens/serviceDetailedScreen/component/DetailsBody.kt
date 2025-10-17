package com.zontechx.servidex.ui.screens.serviceDetailedScreen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@Preview
fun DetailsBody() {

    val TAG = "DetailsBody"

//    val tabs = listOf("About", "Review")
    val tabs = listOf("About")
    val totalPages = tabs.size
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { totalPages }
    )
    
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 48.dp) // Ensure content is above system gesture bar
    ) {
        TabRow(
            containerColor = AppColor.White,
            contentColor = AppColor.Primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = AppColor.Primary
                )
            },
            selectedTabIndex = pagerState.currentPage
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                                fontSize = 16.sp,
                                color = if (pagerState.currentPage == index) AppColor.Primary else AppColor.gray_4
                            )
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            beyondViewportPageCount = 2,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            key = { page -> page }
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                when (page) {
                    0 -> About()
                    1 -> Review()
                }
            }
        }
    }
}