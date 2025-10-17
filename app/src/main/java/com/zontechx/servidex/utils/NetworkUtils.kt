package com.zontechx.servidex.utils
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object NetworkUtils {
    private val TAG = "NetworkUtils"
    private val client = OkHttpClient()

    suspend fun getPublicIp(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://api.ipify.org")
                    .build()
                val response = client.newCall(request).execute()
                response.body?.string()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getGeoFromIp(ip: String): Triple<String?, String?, String?> = withContext(Dispatchers.IO) {
        try {
            // ip-api is free for low-volume use. Example fields: country, regionName (state), city, query (ip)
            val url = "http://ip-api.com/json/$ip?fields=status,country,regionName,city,query,message"
            val req = Request.Builder().url(url).build()
            client.newCall(req).execute().use { resp ->
                Log.e(TAG, "getGeoFromIp: $resp")
                if (!resp.isSuccessful) return@withContext Triple(null, null, null)
                val body = resp.body?.string() ?: return@withContext Triple(null, null, null)
                val json = JSONObject(body)
                Log.e(TAG, "json: $json")
                if (json.optString("status") == "success") {
                    val country = json.optString("country")
                    val region = json.optString("regionName") // state
                    val city = json.optString("city")
                    Log.e(TAG, "country: $country")
                    Triple(country, region, city)
                } else {
                    // error
                    Log.e(TAG, "Error: ${json.optString("message")}")
                    Triple(null, null, json.optString("message"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Exception: ${e.message}")
            Triple(null, null, null)
        }
    }
}