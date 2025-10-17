package com.zontechx.servidex.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InAppStore {

    private val Context.dataStore by preferencesDataStore(name = AppConfig.APP_NAME)

    private val gson = Gson()

    // Save String
    fun saveString(context: Context, key: String, value: String) {
        runBlocking {
            val dataStoreKey = stringPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[dataStoreKey] = value
            }
        }
    }

    // Get String
    fun getString(context: Context, key: String): String? {
        return runBlocking {
            val dataStoreKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[dataStoreKey]
        }
    }

    // Save Int
    fun saveInt(context: Context, key: String, value: Int) {
        runBlocking {
            val dataStoreKey = intPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[dataStoreKey] = value
            }
        }
    }

    // Get Int
    fun getInt(context: Context, key: String): Int? {
        return runBlocking {
            val dataStoreKey = intPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[dataStoreKey]
        }
    }

    // Save Boolean
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        runBlocking {
            val dataStoreKey = booleanPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[dataStoreKey] = value
            }
        }
    }

    // Get Boolean
    fun getBoolean(context: Context, key: String): Boolean? {
        return runBlocking {
            val dataStoreKey = booleanPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[dataStoreKey]
        }
    }

    // Save Model Object
    fun <T> saveObject(context: Context, key: String, obj: T) {
        val jsonString = gson.toJson(obj)
        saveString(context, key, jsonString)
    }

    // Get Model Object
    fun <T> getObject(context: Context, key: String, clazz: Class<T>): T? {
        val jsonString = getString(context, key)
        return if (jsonString != null) gson.fromJson(jsonString, clazz) else null
    }

    // Clear a single key
    fun clearKey(context: Context, key: String) {
        runBlocking {
            val preferencesKey = stringPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences.remove(preferencesKey)
            }
        }
    }

    // Clear All Data
    fun clearAll(context: Context) {
        runBlocking {
            context.dataStore.edit { it.clear() }
        }
    }
}
