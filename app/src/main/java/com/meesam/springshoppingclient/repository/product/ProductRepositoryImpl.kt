package com.meesam.springshoppingclient.repository.product

import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.network.ProductApi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) : ProductRepository {
    override suspend fun getAllProduct(): Response<List<ProductResponse>> {
        return productApi.getAllProduct()
    }

    override suspend fun getProductById(productId: Long): Flow<ProductResponse> = flow {
        val result = productApi.getProductById(productId = productId)
        if (result.isSuccessful){
          emit(result.body()!!)
        }
    }
}