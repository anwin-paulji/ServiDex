package com.zontechx.servidex.service.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zontechx.servidex.MainActivity
import com.zontechx.servidex.R

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingService"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New token: $token")

        // Send this token to your backend server
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {}

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Message received: $message")
        showNotification(message.notification?.title ?: "New Notification", message.notification?.body ?: "")
    }

    private fun showNotification(title: String, message: String) {

        val fullScreenIntent = Intent(this, MainActivity::class.java)
        val channelId = "default_channel"
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8+
        val channel = NotificationChannel(
            channelId,
            "Default Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)

        manager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}