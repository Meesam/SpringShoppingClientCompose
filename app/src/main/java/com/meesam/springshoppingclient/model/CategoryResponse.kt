package com.meesam.springshoppingclient.model

data class CategoryResponse(
    val id: Long,
    val title: String,
    val createdAt: String,
    val isSelected: Boolean = false
)
