package com.meesam.springshoppingclient.repository.product

import com.meesam.springshoppingclient.model.ProductResponse
import retrofit2.Response
import retrofit2.http.Query

interface ProductRepository {
    suspend fun getAllProduct(): Response<List<ProductResponse>>

    suspend fun getProductById(productId: Long): Response<ProductResponse>
}