package com.zontechx.servidex.ui.provider.providerDashboard.component

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.AppConfig
import com.zontechx.servidex.utils.AppUtils.areNotificationsEnabled

@Composable
fun DrawerContent() {
    val TAG = "ToolbarStart"
    var context = LocalContext.current
    val rootNavController = LocalRootNavController.current
    val imageLoader = ImageLoader.Builder(context).build()
    var userName by remember { mutableStateOf("(Provider)") }
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val locationData = sharedVM.currentAddress.collectAsState()

    var profileImage = AppConfig.APP_LOGG_URL
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.75f

    val name_TextStyle =
        TextStyle(color = AppColor.Black, fontSize = 18.sp, fontWeight = FontWeight.W800)
    val location_TextStyle =
        TextStyle(color = AppColor.Black, fontSize = 14.sp, fontWeight = FontWeight.W400)

    val lcoationText_Modifier = Modifier.padding(top = 5.dp)

    val shouldRequest = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    var showNotificationPermission by remember { mutableStateOf(false) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Notifications Allowed")
            showNotificationPermission = false  && shouldRequest
            Toast.makeText(context, "Notifications Allowed ✅", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Notifications Denied")
            showNotificationPermission = true  && shouldRequest
            Toast.makeText(context, "Notifications Denied ❌", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleHomeNavigation() {
        rootNavController.navigate(Screen.MainScreen.route) {
            popUpTo(0) { inclusive = true } // clear everything
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        showNotificationPermission = areNotificationsEnabled(context).not() && shouldRequest
    }

    ModalDrawerSheet(
        drawerContainerColor = AppColor.White,
        modifier = Modifier
            .width(drawerWidth)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColor.White)
                .navigationBarsPadding()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(profileImage)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image from URL",
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .size(60.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = AppColor.yellow_1),
                        contentScale = ContentScale.Crop
                    )

                    Text(text = userName, style = name_TextStyle)

                    locationData.value?.let { address ->
                        Text(
                            text = address.locationName,
                            style = location_TextStyle,
                            modifier = lcoationText_Modifier
                        )
                    }


                    AnimatedButton(
                        text = AppConstant.SWITCH_TO_HOME,
                        backgroundColor = AppColor.White,
                        textColor = AppColor.Black,
                        buttonBorderColor = AppColor.gray_4,
                        onClick = { handleHomeNavigation() },
                        paddingEnd = 0.dp,
                        paddingStart = 0.dp,
                        paddingTop = 10.dp,
                        textSyle = TextStyle(
                            color = AppColor.Black,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_medium))
                        ),
                        textPaddingValues = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 8.dp,
                            top = 8.dp
                        )
                    )

                    Divider(
                        color = AppColor.gray_1,
                        thickness = 0.2.dp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    if(showNotificationPermission) {
                        EnableNotification {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    DrawerContent()
}

@Preview
@Composable
fun EnableNotification(onClick: () -> Unit = {}){
    Column(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = AppColor.white_2)
            .padding(top = 4.dp, start = 4.dp, end = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = AppConstant.ENABLE_NOTIFICATION,
            style = TextStyle(
                color = AppColor.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto_medium))
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = AppConstant.NOTIFICATION_DESCRIPTION,
            style = TextStyle(
                color = AppColor.gray_4,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        )
        AnimatedButton(
            text = "Enable",
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 6.dp),
            paddingStart = 4.dp,
            paddingEnd = 4.dp,
            textPaddingValues = PaddingValues(start = 15.dp, end = 15.dp, bottom = 8.dp, top = 8.dp),
            onClick = { onClick() })
    }
}