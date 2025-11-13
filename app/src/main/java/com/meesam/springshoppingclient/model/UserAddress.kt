package com.meesam.springshoppingclient.model

data class UserAddress(
    val addressType: String = "",
    val address: String = "",
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val zipCode: String = "",
    val street: String = "",
    val comment: String? = null,
    val nearBy: String? = null,
    val isPrimary: Boolean = false,
    val contactPerson: String,
    val contactNumber: String
)
