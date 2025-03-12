package com.zontechx.servidex.base

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.zontechx.servidex.AppConfig
import com.zontechx.servidex.repo.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConfig.APP_NAME)

class MyApplication: Application() {
    lateinit var userRepo: UserRepository

    override fun onCreate() {
        super.onCreate()
        userRepo = UserRepository(dataStore)
    }
}