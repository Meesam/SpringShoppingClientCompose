package com.meesam.springshoppingclient.model

data class ProductResponse(
    val id: Long? = null,
    val title: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val categoryId: Long? = null,
    val categoryName: String = "",
    val quantity: Int = 0,
    val createdAt: String? = null,
    val isActive: Boolean,
    val productImages: Long? = null,
    val productAttributes: Long? = null,
    val productImagesList: List<ProductImagesResponse>? = emptyList(),
    val productAttributesList: List<ProductAttributesResponse>? = emptyList()
)
