package com.zontechx.servidex.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast

object NotificationUtils {

    // Check if notification access is granted
    fun isNotificationAccessGranted(context: Context): Boolean {
        val cn = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = context.packageName
        return !TextUtils.isEmpty(cn) && cn.contains(packageName)
    }

    // Request user to enable notification access
    fun requestNotificationAccess(activity: Activity) {
        Toast.makeText(activity, "Please enable notification access", Toast.LENGTH_LONG).show()
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        activity.startActivity(intent)
    }
}