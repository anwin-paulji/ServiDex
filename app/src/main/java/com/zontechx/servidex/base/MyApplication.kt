package com.zontechx.servidex.base

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.zontechx.servidex.repo.UserRepository
import com.zontechx.servidex.service.firebase.NotificationChannelManager
import com.zontechx.servidex.utils.AppConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConfig.APP_NAME)

class MyApplication: Application() {
    lateinit var firebaseAnalytics: FirebaseAnalytics;
    lateinit var userRepo: UserRepository

    override fun onCreate() {
        super.onCreate()
//        firebaseAnalytics = Firebase.analytics;
        userRepo = UserRepository(dataStore)
        NotificationChannelManager.createAllChannels(this)
    }
}