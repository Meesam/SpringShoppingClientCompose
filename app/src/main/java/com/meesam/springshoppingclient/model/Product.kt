package com.meesam.springshoppingclient.model

data class Product(
    var id: String ="",
    val title: String = "",
    val description: String ="",
    val price: Double = 0.0,
    val category: String = ""
)
