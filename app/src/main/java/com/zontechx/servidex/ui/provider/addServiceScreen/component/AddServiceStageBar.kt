package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun AddServiceStageBar(onPagerChange: (Int) -> Unit = {}) {

    val totalPages = 3
    val pagerState = rememberPagerState(initialPage = 0) {
        totalPages
    }

    LaunchedEffect(pagerState.currentPage) {
        onPagerChange(pagerState.currentPage)
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.white_2)
    ) {

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (page) {

                    0 -> ServiceDetailsForm(pagerState)

                    1 -> ServiceImage()

                    2 -> LegalAgreementForm()
                }
            }
        }
    }
}

@Preview
@Composable
fun AddServiceStageBarPreview() {
    AddServiceStageBar()
}