package com.zontechx.servidex.utils

import com.zontechx.servidex.BuildConfig

object AppConfig {

    val APP_VERSION_NAME = BuildConfig.VERSION_NAME
    val APP_VERSION_CODE = BuildConfig.VERSION_CODE

    val APP_NAME = "ServiDEX";
    val APP_PACKAGE_NAME = "com.zontechx.servidex";
    val APP_LOGG_URL = "https://servidex.s3.ap-south-1.amazonaws.com/icons/ASSET_1760638503822"

    val API_ENVIRONMENT = ApiEnvironment.DEVELOPMENT

    val BASE_URL: String

    init {
        when(API_ENVIRONMENT) {
            ApiEnvironment.PRODUCTION -> {
                BASE_URL = ""
            }
            ApiEnvironment.DEVELOPMENT -> {
                BASE_URL = "http://65.0.66.57:8088/api/"
//                BASE_URL = "http://192.168.1.3:8088/api/"
            }
        }
    }
}

enum class ApiEnvironment {
    DEVELOPMENT,
    PRODUCTION
}