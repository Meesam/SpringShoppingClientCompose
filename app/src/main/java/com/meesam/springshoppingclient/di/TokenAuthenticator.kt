package com.meesam.springshoppingclient.di

import android.util.Log
import com.google.gson.Gson
import com.meesam.springshoppingclient.model.AuthRefreshTokenRequest
import com.meesam.springshoppingclient.network.AuthApiService
import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    // Inject your ApiService or a dedicated RefreshTokenService
    private val authApiService: AuthApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Check if the request already failed with a token refresh attempt
        //    This is to prevent an infinite loop if the refresh token itself is invalid.
        if (response.request.header("Authorization") != null && response.priorResponse != null) {
            return null
        }

        // 2. Get the current refresh token
        val refreshToken = tokenManager.getToken(Constants.REFRESH_TOKEN) ?: return null
        val newTokens = runBlocking {
            try {
                val request = AuthRefreshTokenRequest(
                    token = refreshToken
                )
                authApiService.refreshToken(request)
            } catch (e: Exception) {
                // Handle refresh token API call failure (e.g., log out user)
                tokenManager.clearPref()
                null
            }
        } ?: return null // Refresh token call failed or returned no tokens

        // 4. Save the new tokens
        if(newTokens.isSuccessful && newTokens.body() != null){
            newTokens.body()?.let { newToken->
                Log.d("Refresh Token Call",newToken.accessToken)
                tokenManager.saveToken(newToken.accessToken, Constants.ACCESS_TOKEN)
                tokenManager.saveToken(newToken.refreshToken, Constants.REFRESH_TOKEN)
                val userDetailString = Gson().toJson(newToken.user)
                tokenManager.saveUserDetail(userDetailString)
            }
        }
        // 5. Retry the original request with the new access token
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.body()?.accessToken}")
            .build()
    }
}

