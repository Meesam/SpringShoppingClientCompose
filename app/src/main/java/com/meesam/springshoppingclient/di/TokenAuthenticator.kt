package com.meesam.springshoppingclient.di

import android.util.Log
import com.google.gson.Gson
import com.meesam.springshoppingclient.model.AuthRefreshTokenRequest
import com.meesam.springshoppingclient.network.AuthApiService
import com.meesam.springshoppingclient.pref.ACCESS_TOKEN_KEY
import com.meesam.springshoppingclient.pref.REFRESH_TOKEN_KEY
import com.meesam.springshoppingclient.pref.USER_DETAILS_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TokenAuthenticator @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authApiService: AuthApiService
) : Authenticator {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val accessTokenFromPreferences = userPreferences.getPref(key = ACCESS_TOKEN_KEY)
    val refreshTokenFromPreferences = userPreferences.getPref(key = REFRESH_TOKEN_KEY)
    var accessToken: String = ""
        private set
    var refreshToken: String = ""
        private set

    init {
        scope.launch {
            accessTokenFromPreferences.collect { acToken->
                accessToken = acToken
            }
        }
        scope.launch {
            refreshTokenFromPreferences.collect {rfToken->
                refreshToken = rfToken
            }
        }
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Check if the request already failed with a token refresh attempt
        //    This is to prevent an infinite loop if the refresh token itself is invalid.
        if (response.request.header("Authorization") != null && response.priorResponse != null) {
            return null
        }

        val newTokens = runBlocking {
            try {
                val request = AuthRefreshTokenRequest(
                    token = refreshToken
                )
                authApiService.refreshToken(request)
            } catch (e: Exception) {
                // Handle refresh token API call failure (e.g., log out user)
                userPreferences.clear()
                null
            }
        } ?: return null // Refresh token call failed or returned no tokens

        // 4. Save the new tokens
        if(newTokens.isSuccessful && newTokens.body() != null){
            newTokens.body()?.let { newToken->
                scope.launch {
                    Log.d("Refresh Token Call",newToken.accessToken)
                    userPreferences.savePref(pref = newToken.accessToken, key = ACCESS_TOKEN_KEY)
                    userPreferences.savePref(pref = newToken.refreshToken, key = REFRESH_TOKEN_KEY)
                    val userDetailString = Gson().toJson(newToken.user)
                    userPreferences.savePref(key = USER_DETAILS_KEY, pref = userDetailString)
                }

            }
        }
        // 5. Retry the original request with the new access token
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.body()?.accessToken}")
            .build()
    }
}

