package com.meesam.springshoppingclient.model

data class AuthLoginResponse(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String,
    val user: UserResponse? =null
)
