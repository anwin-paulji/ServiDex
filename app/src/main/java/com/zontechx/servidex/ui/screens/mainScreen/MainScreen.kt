package com.zontechx.servidex.ui.screens.mainScreen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.AddressData
import com.zontechx.servidex.service.local.AppStorage
import com.zontechx.servidex.ui.components.LocationPickerSheet
import com.zontechx.servidex.ui.screens.mainScreen.component.CreateBusinessConfirmationSheet
import com.zontechx.servidex.ui.screens.mainScreen.component.DrawerContent
import com.zontechx.servidex.ui.screens.mainScreen.component.LocationSelectionSheet
import com.zontechx.servidex.ui.screens.mainScreen.component.LogoutSheet
import com.zontechx.servidex.ui.screens.mainScreen.page.categoryScreen.CategoryScreen
import com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.HomeScreen
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.FirebaseUtils
import com.zontechx.servidex.utils.InAppStore
import com.zontechx.servidex.utils.NetworkUtils
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "Home"
    const val CATEGORY = "Category"
    const val BOOKING = "Booking"
    const val CHAT = "Chat"
    const val MY_SERVICE = "My Service"
}

val LocalMainVM = staticCompositionLocalOf<MainScreenVM> {
    error("No ViewModel provided")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: NavHostController) {

    val TAG = "MainScreen"
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val vm: MainScreenVM = viewModel()
    val showLocationSheet by vm.showLocationSheet.collectAsState()
    val sharedVM: SharedVM = LocalSharedViewModel.current

    val locationPickerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, confirmValueChange = { newValue -> false })
    var showLocationPickerSheet by remember { mutableStateOf(false) }
    val locationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val totalPages = 3
    val pagerState = rememberPagerState(initialPage = 0) { totalPages }

    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }
    val closeDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }

    var ip: String? = null
    LaunchedEffect(Unit) {
        scope.launch {
            ip = NetworkUtils.getPublicIp()
            Log.d(TAG, "Public IP: $ip")
            if (ip != null) {
                val (country, state, cityOrError) = NetworkUtils.getGeoFromIp(ip!!)
                Log.d(TAG, "IP=$ip country=$country state=$state cityOrError=$cityOrError")
            } else {
                Log.d(TAG, "Could not fetch public IP")
            }
        }
    }

    /** Logout from side drawer */
    val doLogOUt: () -> Unit = {
        InAppStore.saveBoolean(context = context, key = AppKey.IS_LOGIN, value = false)
        InAppStore.saveString(context = context, key = AppKey.FCM_TOKEN, value = "")
        AppStorage.clearToken()
        FirebaseUtils.deleteFCMToken()
        rootNavController.navigate(Screen.LoginRegisterScreen.route)
    }

    BackHandler {
        when {
            drawerState.isOpen -> {
                scope.launch { drawerState.close() }
            }

            locationSheetState.isVisible -> {
                scope.launch { locationSheetState.hide() }
            }

            pagerState.currentPage != 0 -> {
                scope.launch { pagerState.scrollToPage(0) }
            }

            else -> {
                (context as? Activity)?.finish()
            }
        }
    }

    LaunchedEffect(Unit) {
        // Store
        InAppStore.saveString(
            context = context,
            key = AppKey.LAST_LAUNCHED_SCREEN,
            value = AppConstant.ScreenName.HOME_SCREEN
        )
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "Token: $token")
        }
    }

    LaunchedEffect(Unit) {

        when (sharedVM.currentAddress) {
            is AddressData.Success -> {
                val address = sharedVM.currentAddress as AddressData.Success
                vm.getDashboardData(address.address.latitude, address.address.longitude)
            }

            is AddressData.Failure -> {
                // Todo
                vm.getDashboardData(0.0, 0.0)
            }
        }
        vm.getCategoryList()
    }

    LaunchedEffect(showLocationSheet) {
        if (showLocationSheet == true) {
            locationSheetState.show()
        } else {
            locationSheetState.hide()
        }
    }

    CompositionLocalProvider(LocalMainVM provides vm) {
        ModalNavigationDrawer(
            drawerState = drawerState, drawerContent = {
                DrawerContent(onClickSwitchToBusiness = {
                    closeDrawer()
                    rootNavController.navigate(Screen.ProviderDashboardScreen.route)
                }, onClickCreateBusiness = {
                    closeDrawer()
                    CreateBusinessConfirmationSheet.show()
                }, onClickLogoutButton = {
                    closeDrawer()
                    LogoutSheet.show()
                })
            }, gesturesEnabled = true
        ) {
            Scaffold(bottomBar = { BottomBar(pagerState) }) { innerPadding ->

                HorizontalPager(
                    userScrollEnabled = false,
                    beyondViewportPageCount = 2,
                    state = pagerState
                ) { page ->
                    when (page) {
                        0 -> {
                            HomeScreen(
                                bottomNavPagerState = pagerState,
                                onClickProfile = { openDrawer() })
                        }

                        1 -> CategoryScreen()
//                        2 -> BookingScreen()
//                        3 -> ChatScreen()
                    }
                }
            }
        }
    }

    if (showLocationSheet) {
        LocationSelectionSheet(
            sheetState = locationSheetState,
            onDismiss = { vm.onClickLocationSheet(false) },
            onClick = { vm.onClickLocationSheet(true) },
            onclickSelectFromMap = { showLocationPickerSheet = true })
    }

    LogoutSheet.Register(onClickButton1 = {
        doLogOUt()
    }, onClickButton2 = {})
    CreateBusinessConfirmationSheet.Register(onClickButton1 = {
        rootNavController.navigate(Screen.AddServiceScreen.route)
    }, onClickButton2 = {
        rootNavController.navigate(Screen.ProviderDashboardScreen.route)
    })
    /**===========================================================================================*/

    if (showLocationPickerSheet == true) {
        LocationPickerSheet(
            onLocationSelected = { locationData ->
                sharedVM.setCurrentAddress(address = locationData)
                vm.onClickLocationSheet(false)
            },
            sheetState = locationPickerSheetState,
            onDismiss = { showLocationPickerSheet = false })
    }
    /**===========================================================================================*/
}

@Composable
fun BottomBar(pagerState: PagerState) {

    val scope = rememberCoroutineScope()
    val selectedItem = pagerState.currentPage
    val items = listOf(
        Routes.HOME,
        Routes.CATEGORY,
//        Routes.BOOKING,
//        Routes.CHAT
    )

    val selectedIcons = listOf(
        R.drawable.home,
        R.drawable.category,
//        R.drawable.booking,
//            R.drawable.chat
    )

    fun onTabSelected(index: Int) {
        scope.launch {
            pagerState.animateScrollToPage(index)
        }
    }

    Column {
        Divider(
            color = AppColor.gray_1, thickness = 0.2.dp, modifier = Modifier.fillMaxWidth()
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
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = {
                        onTabSelected(index)
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

