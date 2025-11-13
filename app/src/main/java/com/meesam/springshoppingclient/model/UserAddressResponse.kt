package com.meesam.springshoppingclient.model

data class UserAddressResponse(
    val addressType: String? = null,
    val id: Long? = null,
    val city: String? = null,
    val state: String? = null,
    val pin: String? = null,
    val street: String? = null,
    val nearBy: String? = null,
    val country: String? = null,
    val userId: Long? = null,
    val contactName: String? = null,
    val isPrimary: Boolean = false,
    val contactNumber: String? = null,
    val createdAt: String? = null
)
