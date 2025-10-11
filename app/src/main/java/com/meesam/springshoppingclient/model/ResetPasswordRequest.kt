package com.meesam.springshoppingclient.model

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)
