package com.meesam.springshoppingclient.network

import com.meesam.springshoppingclient.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("product/getAll")
    suspend fun getAllProduct(): Response<List<ProductResponse>>

    @GET("product/get-by-id")
    suspend fun getProductById(@Query("productId") productId: Long): Response<ProductResponse>
}