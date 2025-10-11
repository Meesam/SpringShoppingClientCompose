package com.meesam.springshoppingclient.network

import com.meesam.springshoppingclient.model.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("category/all")
    suspend fun getAllCategory(): Response<List<CategoryResponse>>
}