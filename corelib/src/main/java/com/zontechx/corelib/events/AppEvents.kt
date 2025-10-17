package com.zontechx.corelib.events

import android.content.Context
import android.content.res.Resources
import android.icu.util.TimeZone
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlinx.coroutines.SupervisorJob

object AppEvents {

    private const val TAG = "AppEvents"

    private var endpoint: String = "https://example.com/in-app-events"
    private var userId: String? = null

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun init(context: Context, endpoint: String, userId: String? = null) {
        this.endpoint = endpoint
        this.userId = userId
    }

    fun identifyUser(userId: String) {
        this.userId = userId
    }

    fun trackEvent(name: String, properties: Map<String, Any>? = null) {
        scope.launch {
            var conn: HttpURLConnection? = null
            try {
                val json = JSONObject().apply {
                    put("eventName", name)
                    put("eventType", "in-app")
                    put("deviceType", "android")
//                    put("appVersion", BuildConfig.VERSION_NAME)
                    put("platform", "android")
                    put("osVersion", android.os.Build.VERSION.RELEASE)
                    put("manufacturer", android.os.Build.MANUFACTURER)
                    put("model", android.os.Build.MODEL)
                    put("screenHeight", Resources.getSystem().displayMetrics.heightPixels)
                    put("screenWidth", Resources.getSystem().displayMetrics.widthPixels)
                    put("density", Resources.getSystem().displayMetrics.density)
                    put("densityDpi", Resources.getSystem().displayMetrics.densityDpi)
                    put("language", Locale.getDefault().language)
                    put("country", Locale.getDefault().country)
                    put("timezone", TimeZone.getDefault().id)
                    put("locale", Locale.getDefault().toString())
                    put("timestamp", System.currentTimeMillis())
                    put("userId", userId ?: "anonymous")
                    properties?.forEach { (k, v) -> put(k, v) }
                }

                conn = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    connectTimeout = 5000
                    readTimeout = 5000
                    doOutput = true
                }

                conn.outputStream.use { output ->
                    output.write(json.toString().toByteArray())
                    output.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Event tracked: $name")
                } else {
                    Log.e(TAG, "Failed to track event. Response code: $responseCode")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error tracking event", e)
            } finally {
                conn?.disconnect()
            }
        }
    }
}
