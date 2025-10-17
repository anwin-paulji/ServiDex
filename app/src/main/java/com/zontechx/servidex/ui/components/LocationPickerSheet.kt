package com.zontechx.servidex.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.model.LocationData
import com.zontechx.servidex.model.sealed.AddressResponse
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.CurrentAddress
import com.zontechx.servidex.utils.LocationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerSheet(
    sheetState: SheetState,
    onLocationSelected: (LocationData) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val TAG = "LocationPickerSheet"
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        containerColor = AppColor.Black,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        contentColor = Color.White,
        dragHandle = { }) {
        LocationPickerContnet(onLocationSelected = {
            scope.launch {
                sheetState.hide()
                onDismiss()
                onLocationSelected(it)
            }
        }, onDismiss = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        })
    }
}

@Preview
@Composable
fun DragHandler() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .background(color = AppColor.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        RoundIconButton(
            iconRes = R.drawable.arrow_down, backgroundColor = AppColor.transparent_black
        )
    }
}

@Preview
@Composable
fun LocationPickerContnet(
    onLocationSelected: (LocationData) -> Unit = {}, onDismiss: () -> Unit = {}
) {
    val TAG = "LocationPickerContnet"
    val sharedVM: SharedVM = LocalSharedViewModel.current
    var slectedLocation by remember { mutableStateOf<CurrentAddress?>(null) }

    fun handleSubmit() {
        slectedLocation?.let {
            val locationData = LocationData(
                locationName = it.locality, latitude = it.latitude, longitude = it.longitude
            )
            onLocationSelected(locationData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {

            MyMapView { it -> slectedLocation = it }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                RoundIconButton(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(end = 16.dp)
                        .clickable { onDismiss() },
                    iconRes = R.drawable.ic_close,
                    backgroundColor = AppColor.Black,
                    iconTint = AppColor.White
                )

                slectedLocation?.let { SelectedAddress(address = it) }
            }


            AnimatedButton(
                text = "Add Service Location",
                onClick = { handleSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 10.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun MyMapView(onLocationSelected: (CurrentAddress) -> Unit = {}) {

    val TAG = "MyMapView"
    val context = LocalContext.current
    val sharedVM: SharedVM = LocalSharedViewModel.current
    var latLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var isMapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 20f)
    }

    LaunchedEffect(Unit) {
        sharedVM.currentAddress.collect {
            if (it != null && it.latitude != 0.0 && it.longitude != 0.0) {
                latLng = LatLng(it.latitude, it.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 20f)
            }
        }
    }

    // Animate camera if latLng changes
    LaunchedEffect(isMapLoaded, latLng) {
        if (isMapLoaded) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLng, 20f)
            )
        }
    }

    // On camera stop, get center
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving && isMapLoaded) {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.center?.let {
                val result = LocationUtils.getAddressFromLatLng(context, it.latitude, it.longitude)
                if (result is AddressResponse.Success) {
                    onLocationSelected(result.address)
                    Log.e(TAG, "Received Location: ${result.address}")
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true },
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = true,
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = false
            )
        )

        Image(
            painter = painterResource(id = R.drawable.map_pin),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(38.dp)
        )
    }
}