package com.zontechx.servidex.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val currentUserName : Flow<String> = dataStore.data.map {
        value: Preferences ->  value[USER_NAME].toString();
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit {
            preferences -> preferences[USER_NAME] = name
        }
    }
}