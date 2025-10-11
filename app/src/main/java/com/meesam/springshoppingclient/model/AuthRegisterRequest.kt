package com.meesam.springshoppingclient.model

import java.time.LocalDateTime

data class AuthRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role : String? = null,
    val dob: String? = null,
)
