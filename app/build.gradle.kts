plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization") version "2.1.20"
}

android {
    namespace = "com.zontechx.servidex"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zontechx.servidex"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.ktor.client.core.v310)
    implementation(libs.ktor.client.cio.v310)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.kotlinx.serialization.json)
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("io.coil-kt:coil-compose-base:2.7.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

    implementation("com.airbnb.android:lottie-compose:6.6.6")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("com.google.maps.android:maps-compose:6.4.1")
    implementation("com.google.android.gms:play-services-maps:19.1.0")
    implementation("androidx.compose.material3:material3:1.3.0")

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.compose.ui:ui:1.9.2")
    implementation("androidx.compose.foundation:foundation:1.9.2")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.compose:compose-bom:2024.10.00")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")


    //Custom Lib
    implementation(project(":corelib"))
}



