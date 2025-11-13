package com.meesam.springshoppingclient.di

import com.meesam.springshoppingclient.pref.ACCESS_TOKEN_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val userPreferences: UserPreferences
) : Interceptor {

    val accessTokenFromPreferences = userPreferences.getPref(key = ACCESS_TOKEN_KEY)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    var accessToken: String = ""
        private set

    init {
        scope.launch {
            accessTokenFromPreferences.collect {
                accessToken = it
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        accessToken.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(request.build())
    }
}