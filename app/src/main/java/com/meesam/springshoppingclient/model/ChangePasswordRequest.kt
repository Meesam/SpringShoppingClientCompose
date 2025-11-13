package com.meesam.springshoppingclient.model

data class ChangePasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)
