package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.data.model.response.NearByServiceData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.FeedBackSheet
import com.zontechx.servidex.ui.components.dialog.ForceUpdateDialog
import com.zontechx.servidex.ui.screens.mainScreen.LocalMainVM
import com.zontechx.servidex.ui.screens.mainScreen.component.SuggestionAndFeedback
import com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component.NearByService
import com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component.ToolBar
import com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component.TopCategory
import com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.vm.HomePageVM
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.AppConfig.APP_VERSION_CODE
import com.zontechx.servidex.utils.AppConfig.APP_VERSION_NAME
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickProfile: () -> Unit,
    bottomNavPagerState: PagerState,
) {
    val TAG = "HomeScreen"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    var mainScreenVM: MainScreenVM = LocalMainVM.current
    val homePageVM: HomePageVM = viewModel()
    val dashboardDataResponse by mainScreenVM.dashboardData.collectAsState()

    var nearByServiceList by remember { mutableStateOf<NearByServiceData?>(null) }
    // Refresh
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    /** Retry Dashboard API */
    val retryDashboardApi: () -> Unit = {
        sharedVM.currentAddress.value.let {
            if (it != null) {
                mainScreenVM.retryDashboardApi(it.latitude, it.longitude)
            } else {
                mainScreenVM.retryDashboardApi(0.0, 0.0)
            }
        }
    }

    val onRefresh: () -> Unit = {
        isRefreshing = true
        retryDashboardApi()
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFeedbackSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homePageVM.feedbackUpdatedResponse.collect {
            if (it is ApiResponse.Success) {
                showFeedbackSheet = false
                Toast.makeText(context, "Feedback Sent Successfully", Toast.LENGTH_SHORT).show()
            } else if (it is ApiResponse.Error) {
                showFeedbackSheet = false
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(dashboardDataResponse) {
        when (val responseData = dashboardDataResponse) {
            is ApiResponse.Success -> {
                isRefreshing = false
                Log.e(TAG, "Dashboard API Success: ${responseData.data}")
                nearByServiceList = responseData.data.data?.nearByService

                //For Force Update
                val responseVersionData =
                    responseData.data.data?.featureService?.data?.find { it.versionName == APP_VERSION_NAME && it.platform == "android" }

                if (responseVersionData != null && APP_VERSION_CODE < responseVersionData.minSupportedVersionCode) {
                    ForceUpdateDialog.show()
                }

                if (responseData.data.data?.userData?.data != null) {
                    sharedVM.setUserData(responseData.data.data.userData.data)
                }
            }

            is ApiResponse.Error -> {
                isRefreshing = false
                Toast.makeText(context, responseData.message, Toast.LENGTH_SHORT).show()
            }

            else -> {
                /** // Ignore Initial or Loading */
            }
        }
    }

    var scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            ToolBar(
                viewModel = mainScreenVM, onProfileClick = {
                    onClickProfile()
                })
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = refreshState,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppColor.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                TopCategory(mainScreenVM) { scope.launch { bottomNavPagerState.animateScrollToPage(1) } }

//                OfferBanner()

                if (nearByServiceList != null && nearByServiceList!!.data.isNotEmpty()) {
                    NearByService(nearByServiceList!!.data)
                }

//                PopularService { navController.navigate(Routes.CATEGORY) }
//
                SuggestionAndFeedback { showFeedbackSheet = true }
//
//                Spacer(modifier = Modifier.height(50.dp))
            }

            if (showFeedbackSheet) {
                FeedBackSheet(
                    sheetState = sheetState,
                    onDismiss = { showFeedbackSheet = false },
                    onFeedBackSubmit = { level, feedback ->
                        homePageVM.sendFeedback(level = level, feedback = feedback)
                    }
                )
            }
        }
    }
}