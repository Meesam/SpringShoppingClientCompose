package com.meesam.springshoppingclient.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserPreferencesImpl (private val dataStore: DataStore<Preferences>) :
    UserPreferences {
    override suspend fun savePref(key: Preferences.Key<String>, pref: String) {
        dataStore.edit {
            it[key] = pref
        }
    }

    override fun getPref(key: Preferences.Key<String>): Flow<String> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[key] ?: ""
        }
    }

    override suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}