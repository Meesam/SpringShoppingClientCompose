package com.meesam.springshoppingclient.model

import java.time.LocalDateTime

data class AuthRegisterResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role : String,
    val dob: LocalDateTime,
    val lastLoginAt: LocalDateTime
)
