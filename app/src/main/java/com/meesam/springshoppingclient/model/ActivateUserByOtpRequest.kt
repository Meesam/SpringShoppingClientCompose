package com.meesam.springshoppingclient.model

data class ActivateUserByOtpRequest(
    val otp: Int,
    val email: String
)
