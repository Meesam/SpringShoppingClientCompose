package com.meesam.springshoppingclient.model

data class UserUpdateRequest(
    val id: Long,
    val name: String? = null,
    val dob: String? = null,
    val phone: String? = null
)
