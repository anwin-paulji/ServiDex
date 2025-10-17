package com.zontechx.servidex.ui.provider.providerDashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.ui.provider.providerDashboard.component.BottomProviderNavigation
import com.zontechx.servidex.ui.provider.providerDashboard.component.DrawerContent
import com.zontechx.servidex.ui.provider.providerDashboard.page.myService.MyService
import com.zontechx.servidex.ui.provider.providerDashboard.page.ProviderChat
import com.zontechx.servidex.ui.provider.providerDashboard.page.ProviderHome
import com.zontechx.servidex.ui.provider.providerDashboard.page.providerBooking.ProviderBooking
import com.zontechx.servidex.ui.provider.providerDashboard.vm.ProviderMainVM
import com.zontechx.servidex.utils.InAppStore
import kotlinx.coroutines.launch

// Create a CompositionLocal
val LocalProviderMainVM = staticCompositionLocalOf<ProviderMainVM> {
    error("No ViewModel provided")
}

@Composable
fun ProviderDashboardScreen() {

    val context = LocalContext.current
    val totalPages = 3
    val pagerState = rememberPagerState(initialPage = 0) { totalPages }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val vm: ProviderMainVM = viewModel()

    LaunchedEffect(Unit) {
        InAppStore.saveString(
            context = context,
            key = AppKey.LAST_LAUNCHED_SCREEN,
            value = AppConstant.ScreenName.PORVIDER_HOME_SCREEN
        )
    }

    CompositionLocalProvider(LocalProviderMainVM provides vm) {
        ModalNavigationDrawer(
            drawerState = drawerState, drawerContent = {
                DrawerContent()
            }, gesturesEnabled = true
        ) {
            Scaffold(
                bottomBar = {
                    BottomProviderNavigation(pagerState) { selectedIndex ->
                        scope.launch {
                            pagerState.animateScrollToPage(selectedIndex)
                        }
                    }
                }
            ) { innerPadding ->
                ProviderDashboardContent(pagerState, drawerState, modifiedPadding = innerPadding)
            }
        }
    }
}

@Composable
fun ProviderDashboardContent(
    pagerState: PagerState,
    drawerState: DrawerState,
    modifiedPadding: PaddingValues
) {

    HorizontalPager(
        modifier = Modifier.padding(bottom = modifiedPadding.calculateBottomPadding()),
        userScrollEnabled = false,
        beyondViewportPageCount = 2,
        state = pagerState
    ) { page ->
        when (page) {
            0 -> ProviderHome(drawerState)
            1 -> MyService()
//            2 -> ProviderBooking()
//            3 -> ProviderChat()
        }
    }
}