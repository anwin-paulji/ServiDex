package com.zontechx.servidex.ui.screens.splashscreen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.InAppStore
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(rootNavController: NavHostController) {
    val tag = "SplashScreen"
    var context = LocalContext.current
    var activity = context as ComponentActivity

    var isLogin: Boolean = InAppStore.getBoolean(activity, AppKey.IS_LOGIN) == true

    LaunchedEffect(key1 = true) {
        delay(1000)
        if(isLogin) {
            rootNavController.navigate(Screen.LocationScreen.route)
        } else {
            rootNavController.navigate(Screen.OnBoardingScreen.route)
        }
    }

    SplashScreenMain()
}

@Preview
@Composable
fun SplashScreenMain() {

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(AppColor.White)
                .padding(top = innerPadding.calculateTopPadding() - innerPadding.calculateTopPadding())
                .fillMaxSize()
        )
        {
            Image(
                painter = painterResource(id = R.drawable.app_logo_main),
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp),
                contentDescription = ""
            )
        }
    }
}