package com.meesam.springshoppingclient.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.meesam.springshoppingclient.utils.Constants.PREFS_TOKEN_FILE

import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String, tokenType: String) {
        prefs.edit {
            putString(tokenType, token)
        }
    }
    fun getToken(tokenType: String): String? {
        return prefs.getString(tokenType, null)
    }

    fun saveUserDetail(userResponse: String){
        prefs.edit {
            putString(Constants.USER_DETAILS, userResponse)
        }
    }

    fun saveTempRegisteredEmail(email: String){
        prefs.edit {
            putString(Constants.TEMP_REGISTER_EMAIL, email)
        }
    }

    fun getTempRegisteredEmail(): String? {
        return prefs.getString(Constants.TEMP_REGISTER_EMAIL, null)
    }

    fun getUserDetails(): String? {
        return prefs.getString(Constants.USER_DETAILS, null)
    }

    fun clearPref(){
        prefs.edit {
            clear()
        }
    }

}