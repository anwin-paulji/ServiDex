package com.zontechx.servidex.utils

import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseUtils {

    private val TAG = "FirebaseUtils"

    fun deleteFCMToken() {
        FirebaseMessaging.getInstance().deleteToken()
    }

    /**
     *  How to Use ?
     *  generateFCMToken { token -> Log.d("FCM", "Got token: $token") }
     * */
    fun generateFCMToken(callback: (String) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { newToken ->
                callback(newToken)
            }
            .addOnFailureListener {
                callback("")
            }
    }

    /**
     *  How to Use ?
     *      LaunchedEffect(Unit) {
     *         val token = generateFCMToken()
     *         Log.d("FCM", "Got token: $token")
     *     }
     * */
    suspend fun generateFCMToken(): String {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    continuation.resume(token)
                }
                .addOnFailureListener {
                    continuation.resume("")
                }
        }
    }
}