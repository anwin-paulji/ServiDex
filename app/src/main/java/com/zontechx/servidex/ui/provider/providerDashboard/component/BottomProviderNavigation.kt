package com.zontechx.servidex.ui.provider.providerDashboard.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.screens.mainScreen.Routes
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun BottomProviderNavigation(
    pagerState: PagerState,
    onTabSelected: (Int) -> Unit = {}
) {

    val TAG = "BottomProviderNavigation"
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(pagerState.currentPage) }

    fun handleOnTabSelection(selectedIndex: Int) {
        selectedItem = selectedIndex
        if (pagerState.currentPage != selectedIndex) {
            onTabSelected(selectedIndex)
        }
    }

    val items = listOf(
        Routes.HOME,
        Routes.MY_SERVICE,
//        Routes.BOOKING,
//        Routes.CHAT
    )

    val selectedIcons =
        listOf(
            R.drawable.home,
            R.drawable.category,
//            R.drawable.booking,
//            R.drawable.chat
        )
    Column {
        Divider(
            color = AppColor.gray_1,
            thickness = 0.2.dp,
            modifier = Modifier.fillMaxWidth()
        )
        NavigationBar(
            contentColor = AppColor.Primary,
            containerColor = AppColor.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .navigationBarsPadding()
                .height(60.dp)
                .fillMaxWidth(),
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(selectedIcons[index]),
                            contentDescription = item,
                            colorFilter = if (selectedItem == index) ColorFilter.tint(AppColor.Primary) else ColorFilter.tint(
                                AppColor.gray
                            ), modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        handleOnTabSelection(index)
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppColor.Primary,
                        selectedTextColor = AppColor.Primary,
                        indicatorColor = AppColor.liteOrange,
                        unselectedIconColor = AppColor.gray,
                        unselectedTextColor = AppColor.gray
                    )
                )
            }
        }
    }
}