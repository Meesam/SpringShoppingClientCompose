package com.meesam.springshoppingclient.model

data class AddUserAddressRequest(
    val addressType: String = "",
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val zipCode: String? = null,
    val street:String,
    val comment:String? = null,
    val nearBy:String? = null,
    val isPrimary:Boolean = false,
    val contactName: String,
    val contactNumber: String,
    val userId: Long = 0
)
