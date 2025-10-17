package com.zontechx.servidex.ui.screens.nearByServiceScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.screens.nearByServiceScreen.components.NearByServiceListItem
import com.zontechx.servidex.ui.screens.nearByServiceScreen.vm.NearByServiceVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM

@Composable
fun NearByServiceScreen() {

    val context = LocalContext.current
    val vm: NearByServiceVM = viewModel()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val rootNavController = LocalRootNavController.current
    val currentAddress = sharedVM.currentAddress.collectAsState()

    LaunchedEffect(Unit) {
        if (currentAddress.value != null) {
            val latitude = currentAddress.value!!.latitude
            val longitude = currentAddress.value!!.longitude
            vm.loadInitial(latitude = latitude, longitude = longitude)
        } else {
            rootNavController.popBackStack()
            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            Toolbar()
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            NearByServiceList()
        }
    }
}

@Preview
@Composable
fun Toolbar() {

    val rootNavController = LocalRootNavController.current

    Row(
        modifier = Modifier
            .background(color = AppColor.White)
            .statusBarsPadding()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        RoundIconButton(
            iconRes = R.drawable.arrow_left,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .clickable { rootNavController.popBackStack() },
            backgroundColor = AppColor.White,
            iconTint = AppColor.Black,
            iconSize = 24.dp
        )

        Text(
            text = "Near by Services",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            style = TextStyle(
                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )
    }
}

@Composable
fun NearByServiceList() {
    val listState = rememberLazyListState()
    val rootNavController = LocalRootNavController.current
    val vm: NearByServiceVM = viewModel()
    val nearByServices by vm.nearByList.collectAsState()
    val hasMore by vm.hasMore
    val isLoadingMore by vm.isLoadingMore


    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
    ) {
        items(nearByServices, key = { it._id }) { serviceItem ->
            NearByServiceListItem(nearByServiceItem = serviceItem) {
                val serviceId = serviceItem._id
                rootNavController.navigate(
                    Screen.ServiceDetailed.createRoute(
                        serviceId = serviceId,
                        source = AppConstant.ScreenSource.FROM_NEAR_BY_SERVICE_SCREEN
                    )
                )
            }
        }

        if (isLoadingMore) {
            item {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    // 👇 Detect scroll end and load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.collect { lastVisibleIndex ->
            if (lastVisibleIndex == nearByServices.lastIndex && hasMore && !isLoadingMore) {
                vm.loadMore()
            }
        }
    }
}