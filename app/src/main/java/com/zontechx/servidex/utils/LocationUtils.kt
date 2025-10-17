package com.zontechx.servidex.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.zontechx.servidex.model.sealed.AddressResponse
import com.zontechx.servidex.model.sealed.LocationStatus
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.AppConfig.APP_PACKAGE_NAME
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import java.util.Locale

object LocationUtils {

    val tag = "LocationUtils"

    suspend fun getLocationSafely(context: Context, fusedLocationClient: FusedLocationProviderClient):LocationStatus {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    LocationStatus.Success(location.latitude, location.longitude)
                } else {
                    LocationStatus.Error("Location is null")
                }
            } catch (e: Exception) {
                LocationStatus.Error("Failed to get location: ${e.message}")
            }
        } else {
            LocationStatus.Error("Permission not granted")
        }
    }
    //https://maps.googleapis.com/maps/api/geocode/json?latlng=12.9308243,80.2358479&key=YOUR_API_KEY
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): AddressResponse {
        Log.d(tag, "#1 getAddressFromLatLng: $latitude, $longitude")
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                Log.d(tag, "#2")

                val address: Address = addresses[0]

                val currentAddress = CurrentAddress(
                    addressLine = address.getAddressLine(0),
                    latitude = address.latitude,
                    longitude = address.longitude,
                    adminArea = address.adminArea?: "",
                    countryCode = address.countryCode?: "",
                    countryName = address.countryName?: "",
                    featureName = address.featureName?: "",
                    locality = address.locality?: "",
                    subLocality = address.subLocality?: "",
                    subAdminArea = address.subAdminArea?: "",
                    postalCode = address.postalCode?: "",
                    thoroughfare = address.thoroughfare?: "",
                    subThoroughfare = address.subThoroughfare?: ""
                )

                return if(address.getAddressLine(0) == null) {
                    Log.d(tag, "#3")
                    AddressResponse.Error("No address found")
                } else {
                    Log.d(tag, "#4")
                    AddressResponse.Success(currentAddress)
                }
            } else {
                Log.d(tag, "#5")
                return AddressResponse.Error("No address found");
            }
        } catch (e: Exception) {
            Log.d(tag, "#6")
            return AddressResponse.Error("Geocoder failed: ${e.message}")
        }
    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkAndRequestGps(
        activity: Activity,
        onGpsEnabled: () -> Unit,
        onGpsDisabled: (ResolvableApiException) -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000L
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(activity)

        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            // GPS is already ON ✅
            onGpsEnabled()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    onGpsDisabled(exception)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }
}

@Serializable
data class CurrentAddress(
    val addressLine: String,
    val latitude: Double,
    val longitude: Double,
    val adminArea: String,
    val countryCode: String,
    val countryName: String,
    val featureName: String,
    val locality: String,
    val subLocality: String? = "",
    val subAdminArea: String,
    val postalCode: String,
    val thoroughfare: String,
    val subThoroughfare: String
)