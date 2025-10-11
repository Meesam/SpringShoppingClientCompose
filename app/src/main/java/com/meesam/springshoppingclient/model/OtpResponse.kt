package com.meesam.springshoppingclient.model

data class OtpResponse(
    val otpSent: Boolean,
    val otp: Int,
    val email:String
)
