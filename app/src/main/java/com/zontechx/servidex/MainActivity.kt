package com.zontechx.servidex

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.zontechx.servidex.service.local.AppStorage
import com.zontechx.servidex.ui.components.UpdateProfileSheet
import com.zontechx.servidex.ui.components.dialog.ForceUpdateDialog
import com.zontechx.servidex.ui.provider.UploadServiceImage.UploadServiceImageScreen
import com.zontechx.servidex.ui.screens.booking.BookingScreen
import com.zontechx.servidex.ui.screens.locationScreen.LocationScreen
import com.zontechx.servidex.ui.screens.loginRegisterScreen.LoginRegisterScreen
import com.zontechx.servidex.ui.screens.notificationScreen.NotificationScreen
import com.zontechx.servidex.ui.screens.onBoardingScreen.OnBoardingScreen
import com.zontechx.servidex.ui.screens.otpVerificationScreen.OtpVerificationScreen
import com.zontechx.servidex.ui.provider.addServiceScreen.AddServiceScreen
import com.zontechx.servidex.ui.provider.providerDashboard.ProviderDashboardScreen
import com.zontechx.servidex.ui.screens.mainScreen.MainScreen
import com.zontechx.servidex.ui.screens.respectiveServiceScreen.RespectiveServiceScreen
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.ServiceDetailedScreen
import com.zontechx.servidex.ui.screens.splashscreen.SplashScreen
import com.zontechx.servidex.ui.theme.ServidexTheme
import com.zontechx.servidex.ui.vm.SharedVM
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.ui.provider.editMyServiceScreen.EditMyServiceScreen
import com.zontechx.servidex.ui.screens.nearByServiceScreen.NearByServiceScreen
import com.zontechx.servidex.ui.screens.webViewScreen.WebViewScreen
import com.zontechx.servidex.utils.AppUtils
import com.zontechx.servidex.utils.AppUtils.initSplashScreen
import com.zontechx.servidex.utils.DeepLink
import com.zontechx.servidex.utils.InAppStore
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        AppStorage.setDeviceDetails(context = this)
        var initLaunchScreen by mutableStateOf(Screen.SplashScreeen.route)
//        var isAppReady by mutableStateOf(false)

        lifecycleScope.launch {
            var isLogin: Boolean = InAppStore.getBoolean(this@MainActivity, AppKey.IS_LOGIN) == true

            if (isLogin) {
                initLaunchScreen = Screen.LocationScreen.route
            } else {
                initLaunchScreen = Screen.OnBoardingScreen.route
            }
//            isAppReady = true
        }

        initSplashScreen(splashScreen)

        val deepLink = AppUtils.parseDeepLink(intent)

        enableEdgeToEdge()

        setContent {
            ServidexTheme {
                LaunchedEffect(Unit) {
                    AppStorage.initToken(this@MainActivity)
                }

//                if (isAppReady) {
                    NavigationStack(deepLink = deepLink, startDestination = initLaunchScreen)
//                }
                ForceUpdateDialog.Register(this@MainActivity) { }
            }
        }
    }
}

// Create a CompositionLocal
val LocalRootNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

val LocalSharedViewModel = staticCompositionLocalOf<SharedVM> {
    error("No NavController provided")
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationStack(deepLink: DeepLink, startDestination: String = Screen.SplashScreeen.route) {
    val TAG = "NavigationStack"
    val rootNavController = rememberNavController()
    val sharedVM: SharedVM = viewModel()
    var context = LocalContext.current
    var activity = context as ComponentActivity
    var isLogin: Boolean = InAppStore.getBoolean(activity, AppKey.IS_LOGIN) == true

    CompositionLocalProvider(LocalSharedViewModel provides sharedVM) {
        CompositionLocalProvider(LocalRootNavController provides rootNavController) {

            LaunchedEffect(deepLink, isLogin) {
                if (isLogin  && deepLink != DeepLink.Unknown) {

                    when (deepLink) {
                        is DeepLink.Service -> {
                            rootNavController.navigate(
                                Screen.ServiceDetailed.createRoute(
                                    serviceId = deepLink.serviceId,
                                    source = AppConstant.ScreenSource.FROM_DEEP_LINK
                                )
                            )
                        }

                        is DeepLink.Profile -> rootNavController.navigate("profile/${deepLink.userId}")
                        DeepLink.Unknown -> {} // no-op or show home
                    }
                }
            }

            AnimatedNavHost(
                navController = rootNavController,
                startDestination = Route.MainApp.route
            ) {
                navigation(
                    startDestination = startDestination,
                    route = Route.MainApp.route
                ) {
                    composable(
                        route = Screen.SplashScreeen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }
                    ) {
                        SplashScreen(rootNavController = rootNavController)
                    }
                    composable(
                        route = Screen.OnBoardingScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        OnBoardingScreen(rootNavController = rootNavController)
                    }

                    composable(
                        route = Screen.LoginRegisterScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        LoginRegisterScreen(rootNavController = rootNavController)
                    }
                    composable(
                        route = Screen.OtpVerificationScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        OtpVerificationScreen(rootNavController = rootNavController)
                    }

                    composable(
                        route = Screen.LocationScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        LocationScreen(rootNavController = rootNavController)
                    }

                    composable(
                        route = Screen.MainScreen.route,
                        enterTransition = {

                            scaleIn(
                                initialScale = 1.5f, // start zoomed out
                                animationSpec = tween(1000)
                            ) + fadeIn(animationSpec = tween(500))

                        },
                        // Working Properly in Release ✅✅✅
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut(
                                targetScale = 1.5f,
                                animationSpec = tween(1000)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        MainScreen(rootNavController = rootNavController)
                    }
                    composable(
                        route = Screen.ServiceDetailed.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        popExitTransition = {
                            // Working Properly in Release ✅✅✅
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(500)
                            )
                        }
                    ) { backStackEntry ->

                        val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
                        val source = backStackEntry.arguments?.getString("source") ?: ""

                        ServiceDetailedScreen(
                            rootNavController = rootNavController,
                            serviceId = serviceId,
                            screenSource = source
                        )
                    }
                    composable(
                        route = Screen.BookingScreen.route,
                        enterTransition = {
                            Log.e(TAG, "BookingScreen : NavGraph: BookingScreen: enterTransition")
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            Log.e(TAG, "BookingScreen : NavGraph: BookingScreen: exitTransition")
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) { backStackEntry ->

                        val serviceJsonData =
                            backStackEntry.arguments?.getString("serviceJsonData") ?: ""
                        val source = backStackEntry.arguments?.getString("source") ?: ""

                        BookingScreen(
                            rootNavController = rootNavController,
                            serviceJsonData = serviceJsonData,
                            screenSource = source
                        )
                    }
                    composable(
                        route = Screen.NotificationScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut(
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        NotificationScreen(rootNavController = rootNavController)
                    }
                    composable(
                        route = Screen.ProviderDashboardScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        ProviderDashboardScreen()
                    }
                    composable(
                        route = Screen.EditMyServiceScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        }) {
                        EditMyServiceScreen()
                    }
                    composable(
                        route = Screen.AddServiceScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        },
                        deepLinks = listOf(navDeepLink {
                            uriPattern = "servidex://open/addService"
                        })
                    ) {
                        AddServiceScreen(rootNavController = rootNavController)
                    }
                    composable(
                        route = Screen.UploadServiceImageScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        },
                    ) { backStackEntry ->

                        val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
                        val source = backStackEntry.arguments?.getString("source") ?: ""
                        UploadServiceImageScreen(
                            serviceId = serviceId,
                            source = source,
                            rootNavController = rootNavController
                        )
                    }
                    composable(
                        route = Screen.RespectiveServiceScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        },
                    ) { backStackEntry ->
                        val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
                        val source = backStackEntry.arguments?.getString("source") ?: ""
                        RespectiveServiceScreen(serviceId = serviceId, source = source)
                    }
                    composable(
                        route = Screen.WebViewScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        },
                    ) { backStackEntry ->
                        val webViewType = backStackEntry.arguments?.getString("webViewType") ?: ""
                        WebViewScreen(webViewType = webViewType)
                    }
                    composable(
                        route = Screen.NearByServiceScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            scaleOut( // Zoom out when SplashScreen exits
                                targetScale = 1.5f,
                                animationSpec = tween(500)
                            ) + fadeOut(animationSpec = tween(500))
                        },
                    ) { backStackEntry ->
                        NearByServiceScreen()
                    }
                }
                navigation(
                    startDestination = Screen.LocationScreen.route,
                    route = Route.ProviderApp.route
                ) {}
                navigation(
                    startDestination = Screen.LocationScreen.route,
                    route = Route.Feature2.route
                ) {}
            }
            UpdateProfileSheet()
        }
    }
}

sealed class Route(val route: String) {
    object MainApp : Route("main_app")
    object ProviderApp : Route("provider_app")
    object Feature2 : Route("feature_two")
}

sealed class Screen(val route: String) {
    object SplashScreeen : Screen("splash_screen")
    object OnBoardingScreen : Screen("on_boarding_screen")
    object LoginRegisterScreen : Screen("login_register_screen")
    object OtpVerificationScreen : Screen("otp_verification_screen")
    object MainScreen : Screen("main_screen")
    object ServiceDetailed : Screen("service_details/{source}/{serviceId}") {
        fun createRoute(serviceId: String, source: String) = "service_details/$source/$serviceId"
    }

    object NotificationScreen : Screen("notification_screen")
    object ProviderDashboardScreen : Screen("provider_dashboard_screen")
    object AddServiceScreen : Screen("add_service_screen")
    object LocationScreen : Screen("location_screen")
    object BookingScreen : Screen("booking_screen/{source}/{serviceJsonData}") {
        fun createRoute(source: String, serviceJsonData: String) =
            "booking_screen/$source/$serviceJsonData"
    }

    object UploadServiceImageScreen : Screen("UploadServiceImageScreen/{source}/{serviceId}") {
        fun createRoute(serviceId: String, source: String) =
            "UploadServiceImageScreen/$source/$serviceId"
    }

    object RespectiveServiceScreen : Screen("RespectiveServiceScreen/{source}/{serviceId}") {
        fun createRoute(serviceId: String, source: String) =
            "RespectiveServiceScreen/$source/$serviceId"
    }

    object EditMyServiceScreen : Screen("edit_my_service_screen")
    object NearByServiceScreen : Screen("near_by_service_screen")
    object WebViewScreen : Screen("web_view_screen/{webViewType}") {
        fun createRoute(webViewType: String) =
            "web_view_screen/$webViewType"
    }
}
