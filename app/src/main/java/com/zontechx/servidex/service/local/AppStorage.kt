package com.zontechx.servidex.service.local

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.utils.InAppStore

object AppStorage {

    var token: String? = null
    var deviceId: String = ""
    var deviceType: String = ""
    var platform: String = "android"
    var osVersion: String = Build.VERSION.RELEASE ?: "Unknown"
    var deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"
    var appVersion: String = ""

    fun initToken(context: Context) {
        if (token == null) {
            InAppStore.getString(context, AppKey.FCM_TOKEN)?.let {
                token = it
            }
        }
    }

    fun setDeviceDetails(context: Context) {

        deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        deviceType =
            if ((context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                "Tablet"
            } else {
                "Mobile"
            }

        appVersion = try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun setToken(context: Context, newToken: String) {
        token = newToken
        InAppStore.saveString(context, AppKey.FCM_TOKEN, newToken)
    }

    fun clearToken() {
        token = null
    }
}
