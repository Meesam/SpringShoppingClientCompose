package com.meesam.springshoppingclient.di

import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = tokenManager.getToken(Constants.ACCESS_TOKEN)
        token?.let{
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}