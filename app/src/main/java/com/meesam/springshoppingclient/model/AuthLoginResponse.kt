package com.meesam.springshoppingclient.model

data class AuthLoginResponse(
    val token: String,
    val refreshToken: String,
    val user: UserResponse? = null
)
