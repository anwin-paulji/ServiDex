package com.zontechx.servidex.service.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zontechx.servidex.utils.AppConfig
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(AppConfig.APP_NAME)

private val USER_NAME = stringPreferencesKey("user_name")

object DataStoreManager {
    fun getUserName(context: Context, name: String, s :  Preferences.Key<String>) {
        context.dataStore.data.map { 
            value: Preferences ->  value[USER_NAME]?: "Unknown";
        }
    }

    suspend fun saveUserName(context: Context, userName:String) {
        context.dataStore.edit {
            preferences -> preferences[USER_NAME] = userName
        }
    }
}