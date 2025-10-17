package com.meesam.springshoppingclient.model

data class ErrorResponse(
    val details: Any = Any(),
    val error: String = "",
    val message: String = "",
    val path: String = "",
    val status: Int = 0,
    val timestamp: String = ""
)