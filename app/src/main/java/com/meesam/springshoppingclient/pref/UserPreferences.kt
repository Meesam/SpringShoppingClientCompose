package com.meesam.springshoppingclient.pref

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

val USER_DETAILS_KEY = stringPreferencesKey("user_details")
val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
val TEMP_EMAIL_KEY = stringPreferencesKey("temp_email")
interface UserPreferences {
    suspend fun savePref(key: Preferences.Key<String>, pref: String)
    fun getPref(key: Preferences.Key<String>): Flow<String>

    suspend fun clear()

}