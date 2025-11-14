package com.meesam.springshoppingclient.repository.product

import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.network.ProductApi
import jakarta.inject.Inject
import retrofit2.Response

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) : ProductRepository {
    override suspend fun getAllProduct(): Response<List<ProductResponse>> {
        return productApi.getAllProduct()
    }

    override suspend fun getProductById(productId: Long): Response<ProductResponse> {
        return productApi.getProductById(productId = productId)
    }
}