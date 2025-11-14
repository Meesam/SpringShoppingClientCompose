package com.meesam.springshoppingclient.model

data class ProductImagesResponse(
    val id:Long,
    val imageUrl:String?=null,
    val productId:Long,
    val isDefaultImage:Boolean
)
