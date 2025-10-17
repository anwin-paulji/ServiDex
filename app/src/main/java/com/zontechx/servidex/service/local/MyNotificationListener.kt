package com.zontechx.servidex.service.local

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val notification = sbn.notification
        val extras = notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        // Check if notification contains your app name
        if (text.contains("YourAppName", ignoreCase = true)) {
            Log.d("NotificationDetected", "Title: $title, Text: $text")
            // Do your action here
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optional: handle notification removal
    }
}
