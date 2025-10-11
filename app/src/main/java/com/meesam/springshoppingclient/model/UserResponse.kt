package com.meesam.springshoppingclient.model

data class UserResponse(
    val dob: String = "",
    val email: String = "",
    val id: Int = 0,
    val lastLoginAt: String = "",
    val name: String = "",
    val profilePicUrl: String? = null,
    val role: String = ""
)