package com.zontechx.servidex.ui.provider.addServiceScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.UiEvent
import com.zontechx.servidex.ui.components.LocationPickerSheet
import com.zontechx.servidex.ui.provider.addServiceScreen.component.AddServiceStageBar
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CategoryListSheet
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CompleteUserProfileSheet
import com.zontechx.servidex.ui.provider.addServiceScreen.component.SubCategoryListSheet
import com.zontechx.servidex.ui.provider.addServiceScreen.component.TopAppBar
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.InAppStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(rootNavController: NavHostController) {

    val TAG = "AddServiceScreen"

    val context = rootNavController.context
    val keyboardController = LocalSoftwareKeyboardController.current

    var filledIndex by remember { mutableIntStateOf(0) }

    val vm: AddServiceScreenVM = viewModel()

    val isDismissTriggered by vm.isDismissTriggered.collectAsState()
    val locationPickerSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, confirmValueChange = { newValue -> false })
//    var showLocationPickerSheet by remember { mutableStateOf(false) }
    val showLocationPickerSheet by vm.showGoogleMapSheet.collectAsState()

    /** Complete user profile sheet dismiss handler */
    val handleOnDismiss = {
        val isProfileCompleted = InAppStore.getBoolean(context, AppKey.IS_PROFILE_COMPLETE)
        if (isProfileCompleted == false) {
            rootNavController.popBackStack()
            Toast.makeText(context, "Please complete your profile first", Toast.LENGTH_SHORT).show()
        }
        vm.reset_CompleteUserProfileSheet()
    }

    LaunchedEffect(Unit) {

        vm.addServiceResponse.collect { it ->
            when (it) {
                is ApiResponse.Success -> {
                    val serviceId: String = it.data.data?.get("serviceId").toString()
                    vm.navigateToUploadServiceImageScreen(serviceId, AppConstant.ScreenSource.FROM_ADD_SERVICE)
                }

                is ApiResponse.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    // observe one-time events
    LaunchedEffect(Unit) {
        vm.event.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    rootNavController.navigate(event.route)
                }

                UiEvent.PopBackStack -> {
                    rootNavController.popBackStack()
                }
                else -> {}
            }
        }
    }

    /** Complete user profile sheet dismiss handler */
    LaunchedEffect(isDismissTriggered) {
        if (isDismissTriggered == true) {
            handleOnDismiss()
        }
    }

//    SetSystemBarColor()
    /** Main Content */
    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColor.white_2)
                .padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding())
        ) {
            Column {
                TopAppBar(filledIndex)
                AddServiceStageBar(onPagerChange = { page -> filledIndex = page })
            }
        }
    }

    /** BottomSheet init */
    CategoryListSheet.Register { }
    SubCategoryListSheet.Register { }
    CompleteUserProfileSheet()

    if (showLocationPickerSheet == true) {
        LocationPickerSheet(
            onLocationSelected = { locationData ->
                vm.addServiceLocation(locationData)
            },
            sheetState = locationPickerSheetState,
            onDismiss = { vm.showGoogleMapBottomSheet(false)})
    }
}

@Composable
private fun SetSystemBarColor() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcon = true
    val backgroundColor = AppColor.White
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = backgroundColor, darkIcons = useDarkIcon
        )
    }
}
