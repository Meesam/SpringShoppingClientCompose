package com.meesam.springshoppingclient.events

sealed class OtpEvents {
    data class OnOtpChange(val otp: String) : OtpEvents()
    data object onVerifyClick :OtpEvents()
}