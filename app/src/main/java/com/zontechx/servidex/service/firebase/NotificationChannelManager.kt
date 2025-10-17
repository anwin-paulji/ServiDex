package com.zontechx.servidex.service.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

object NotificationChannelManager {

    // List all your channel IDs here
    object Channels {
        const val BOOKING = "booking_channel"
        const val CHAT = "chat_channel"
        const val PAYMENT = "payment_channel"
        const val PROMOTION = "promotion_channel"
        const val SYSTEM = "system_channel"
        const val FEEDBACK = "feedback_channel"
        const val REMINDER = "reminder_channel"
        const val ALERT = "alert_channel"
        const val UPDATE = "update_channel"
        const val DEFAULT = "default_channel"
    }

    fun createAllChannels(context: Context) {
        val channels = listOf(
            NotificationChannel(
                Channels.BOOKING,
                "Bookings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about new bookings"
                enableVibration(true)
                lightColor = Color.BLUE
            },

            NotificationChannel(
                Channels.CHAT,
                "Chats",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new chat messages"
                enableVibration(true)
                lightColor = Color.GREEN
            },

            NotificationChannel(
                Channels.PAYMENT,
                "Payments",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Payment updates and confirmations"
            },

            NotificationChannel(
                Channels.PROMOTION,
                "Promotions",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Promotional offers and discounts"
            },

            NotificationChannel(
                Channels.SYSTEM,
                "System Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alerts from Servidex"
                enableVibration(true)
                lightColor = Color.RED
            },

            NotificationChannel(
                Channels.FEEDBACK,
                "Feedback",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Feedback and surveys"
            },

            NotificationChannel(
                Channels.REMINDER,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for scheduled bookings"
                enableVibration(true)
            },

            NotificationChannel(
                Channels.ALERT,
                "Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent alerts that need immediate attention"
                enableVibration(true)
                lightColor = Color.MAGENTA
            },

            NotificationChannel(
                Channels.UPDATE,
                "Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General updates about Servidex"
            },

            NotificationChannel(
                Channels.DEFAULT,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Default notifications"
            }
        )

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannels(channels)
    }
}
