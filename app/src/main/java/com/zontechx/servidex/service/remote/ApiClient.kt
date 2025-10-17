package com.zontechx.servidex.service.remote

import android.util.Log
import com.zontechx.servidex.service.local.AppStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient {
    val tag = "ApiClient"
    val client: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("ApiClient", "log: $message")
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 20000
            }
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
                val token = AppStorage.token
                if (token != null && token.isNotEmpty()) {
                    header("Authorization", "Bearer $token")
                }
                header("appVersion", AppStorage.appVersion)
                header("deviceId", AppStorage.deviceId)
                header("deviceType", AppStorage.deviceType)
                header("deviceModel", AppStorage.deviceModel)
                header("osVersion", AppStorage.osVersion)
                header("platform", AppStorage.platform)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ApiClient? = null

        fun getInstance(): ApiClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiClient().also { INSTANCE = it }
            }
        }
    }
}