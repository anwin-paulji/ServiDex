package com.zontechx.servidex.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen

object AppUtils {

    fun shareData(context: Context, data: String) {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun initSplashScreen(splashScreen: SplashScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView.view,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.view.height.toFloat()
                )

                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                slideUp.doOnEnd { splashScreenView.remove() }
                slideUp.start()
            }
        } else {}
    }

    fun parseDeepLink(intent: Intent?): DeepLink {
        if (intent?.action != Intent.ACTION_VIEW || intent.data == null) return DeepLink.Unknown

        val uri = intent.data!!
        val pathSegments = uri.pathSegments
        val host = uri.host

        // Example: https://servidex.in/service/123
        return when {
            host == "servidex.in" && pathSegments.size >= 2 && pathSegments[0] == "service" -> {
                DeepLink.Service(serviceId = pathSegments[1])
            }

            host == "servidex.in" && pathSegments.size >= 2 && pathSegments[0] == "profile" -> {
                DeepLink.Profile(userId = pathSegments[1])
            }

            // Add more deep link patterns here

            else -> DeepLink.Unknown
        }
    }
}

sealed class DeepLink {
    data class Service(val serviceId: String) : DeepLink()
    data class Profile(val userId: String) : DeepLink()
    object Unknown : DeepLink()
    // Add more deep link types here
}
