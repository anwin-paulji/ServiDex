package com.zontechx.servidex.ui.provider.providerDashboard.page.myService

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.providerDashboard.page.myService.component.MyServiceList
import com.zontechx.servidex.ui.provider.providerDashboard.page.myService.component.Toolbar
import com.zontechx.servidex.ui.provider.providerDashboard.page.myService.vm.MyServiceVM
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyService() {
    val vm: MyServiceVM = viewModel()
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val onRefresh: () -> Unit = {
        isRefreshing = true
        vm.loadInitial()
    }

    LaunchedEffect(Unit) {
        vm.loadInitial()
        isRefreshing = false
    }

    LaunchedEffect(Unit) {
        vm.myServiceUiState.collect { it ->
            when (it) {
                is ApiResponse.Success -> {
                    isRefreshing = false
                }

                is ApiResponse.Error -> {
                    isRefreshing = false
                }

                else -> {}
            }
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = refreshState,
            onRefresh = onRefresh,
            modifier = Modifier
                .background(AppColor.White)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.White)
                    .padding(innerPadding)
            ) {
                Toolbar()
                MyServiceList()
            }
        }
    }
}