package com.zontechx.servidex.ui.screens.locationScreen

//import com.google.accompanist.permissions.rememberPermissionState

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.LocationData
import com.zontechx.servidex.model.sealed.AddressResponse
import com.zontechx.servidex.model.sealed.LocationStatus
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.AppConfig.APP_PACKAGE_NAME
import com.zontechx.servidex.utils.GlobalViewModelProvider
import com.zontechx.servidex.utils.InAppStore
import com.zontechx.servidex.utils.LocationUtils
import com.zontechx.servidex.utils.LocationUtils.checkAndRequestGps
import com.zontechx.servidex.utils.LocationUtils.isLocationPermissionGranted
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Screen for handling location permissions and GPS settings.
 * 1st : Check GPS is enabled or not
 * 2nd : Check Location Permission is granted or not
 * 3rd : If both are granted then get the location
 * 4th : If any of the above is not granted then show the dialog to enable it.
 * 5th : If user denied the permission permanently then show the dialog to go to settings.
 * 6th : If user denied the permission then show the dialog to enable it
 * 7th : If user denied the can move to the next screen.
 * */
@Composable
fun LocationScreen(rootNavController: NavHostController) {
    LocationScreenPreview(navController = rootNavController)
}


@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocationScreenPreview(navController: NavHostController? = null) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.map_icon)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.White)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColor.White)
                .padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding()
                ),
        ) {

            LottieAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(400.dp),
                composition = composition,
                progress = { progress }
            )


            LocationBottomButton(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocationBottomButton(
    modifier: Modifier = Modifier
) {
    val TAG = "LocationBottomButton"
    val context = LocalContext.current
    val rootNavController = LocalRootNavController.current
    val scope = rememberCoroutineScope()
    val activity = context as ComponentActivity
    var gpsStatus by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }

    val fineLocationPermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var locationText by remember { mutableStateOf("No location yet") }
    val sharedVM: SharedVM = LocalSharedViewModel.current
    //-----------------------------------------

    @OptIn(ExperimentalPermissionsApi::class)
    fun handlePermissionResult(
        status: PermissionStatus,
        onGranted: () -> Unit,
        onDenied: (permanently: Boolean) -> Unit
    ) {
        when (status) {
            is PermissionStatus.Granted -> {
                onGranted()
            }

            is PermissionStatus.Denied -> {
                val permanentlyDenied = !status.shouldShowRationale
                onDenied(permanentlyDenied)
            }
        }
    }

    fun navigateToMainScreen() {
        val lastLaunchedScreen = InAppStore.getString(context, key = AppKey.LAST_LAUNCHED_SCREEN)

        if (lastLaunchedScreen.isNullOrEmpty()
                .not() && lastLaunchedScreen == AppConstant.ScreenName.PORVIDER_HOME_SCREEN
        ) {
            rootNavController.navigate(Screen.ProviderDashboardScreen.route) {
                popUpTo(0) { inclusive = true } // clear everything
                launchSingleTop = true
            }
        } else {
            rootNavController.navigate(Screen.MainScreen.route) {
                popUpTo(0) { inclusive = true } // clear everything
                launchSingleTop = true
            }
        }
    }

    fun getLocation() {
        Log.e(TAG, "getLocation")
        scope.launch {
            delay(1000)
            val location = LocationUtils.getLocationSafely(context, fusedLocationClient)

            if (location is LocationStatus.Success) {

                val addressResponse: AddressResponse = LocationUtils.getAddressFromLatLng(
                    context,
                    location.latitude,
                    location.longitude
                )

                if (addressResponse is AddressResponse.Success) {
                    sharedVM.setCurrentAddress(
                        LocationData(
                            locationName = addressResponse.address.locality,
                            latitude = addressResponse.address.latitude,
                            longitude = addressResponse.address.longitude
                        )
                    )
                    locationText = addressResponse.address.addressLine
                    navigateToMainScreen()
                } else {
                    locationText = "#1 ${(addressResponse as? AddressResponse.Error)?.message ?: "Unknown error"}"
                }
            } else {
                Log.e(
                    TAG,
                    "#2 ${(location as? LocationStatus.Error)?.message ?: "Unknown error"}"
                )
                locationText =
                    "#2 ${(location as? LocationStatus.Error)?.message ?: "Unknown error"}"
            }
        }
    }

    fun openLocationSettings(context: Context) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Location Permission Required")
            .setMessage("Please go to Permissions and allow Location access to use this feature.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", APP_PACKAGE_NAME, null)
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel") { _, _ ->
                navigateToMainScreen()
            }
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
    }

    fun onGpsDeniedOrGranted(context: Context) {
        val hasPermission = isLocationPermissionGranted(context)
        val shouldShowRationale = fineLocationPermissionState.status.shouldShowRationale
        if (hasPermission) {
            Log.e(TAG, "Location Permission Granted ✅")
            /**
             * Check if GPS is enabled or not.
             * If GPS is enabled, then we can get the location.
             * */
            if (gpsStatus) {
                getLocation()
            } else {
                navigateToMainScreen()
            }
        } else {
            Log.e(TAG, "Location Permission NOT Granted ❌")
            if (shouldShowRationale) {
                Log.e(TAG, "#1")
                openLocationSettings(context)
            } else {
                Log.e(TAG, "#2")
                permissionRequested = true
                fineLocationPermissionState.launchPermissionRequest()
            }
        }
    }

    /**
     * GPS is enabled or not.
     * If GPS is enabled, then we can get the location.
     * If GPS is not enabled, then we need to ask the user to enable it.
     * */
    /** STEP 2 */
    val resolutionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onGpsEnabled: ✅")
            gpsStatus = true
            onGpsDeniedOrGranted(context)
        } else {
            Log.e(TAG, "onGpsDenied: ❌")
            gpsStatus = false
            onGpsDeniedOrGranted(context)
        }
    }

    /**
     * Check if GPS is enabled or not.
     * If GPS is enabled, then we can get the location.
     * If GPS is not enabled, then we need to ask the user to enable it.
     * */
    /** STEP 1 */
    LaunchedEffect(Unit) {
        checkAndRequestGps(
            activity = activity,
            onGpsEnabled = {
                Log.e(TAG, "GPS already enabled!: ✅")
                gpsStatus = true
                onGpsDeniedOrGranted(context)
            },
            onGpsDisabled = { exception ->
                Log.e(TAG, "GPS already disabled!: ❌")
                gpsStatus = false
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                resolutionLauncher.launch(intentSenderRequest)
            }
        )
    }
//-----------------------------------------
    LaunchedEffect(fineLocationPermissionState.status) {
        if (permissionRequested) {
            when {
                fineLocationPermissionState.status.isGranted -> {
                    Log.d(TAG, "✅ Permission Granted")
                    onGpsDeniedOrGranted(context)
                }

                fineLocationPermissionState.status.shouldShowRationale -> {
                    Log.d(TAG, "❌ Denied but not permanent")
                    navigateToMainScreen()
                }

                else -> {
                    Log.d(TAG, "❌ Permanently denied")
                    navigateToMainScreen()
                }
            }
        }
    }


//    LaunchedEffect(fineLocationPermissionState.status.isGranted) {
//        Log.d(TAG, "LaunchedEffect : ${fineLocationPermissionState.status.isGranted.not()}")
//        when {
//            fineLocationPermissionState.status is PermissionStatus.Denied -> {
//                Log.d(TAG, "Location Permission Denied")
//                navigateToMainScreen()
//            }
//
//            fineLocationPermissionState.status is PermissionStatus.Granted -> {
//                Log.d(TAG, "Location Permission Granted")
//                getLocation()
//            }
//        }
//    }
//-----------------------------------------

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Text(
            text = locationText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 5.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                textAlign = TextAlign.Center
            )
        )

//        Text("GPS Enabled: ${if (gpsStatus) "✅" else "❌"}")


        AnimatedButton(
            text = AppConstant.ENABLE_LOCATION,
            onClick = {
                permissionRequested = true
                fineLocationPermissionState.launchPermissionRequest()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )

        AnimatedButton(
            text = AppConstant.SKIP,
            textColor = AppColor.Primary,
            onClick = { navigateToMainScreen() },
            modifier = Modifier.fillMaxWidth(), backgroundColor = AppColor.White,
        )

        Text(
            text = AppConstant.ENABLE_LOCAION_DESCRIPTION,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 5.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                textAlign = TextAlign.Center
            )
        )
    }
}
