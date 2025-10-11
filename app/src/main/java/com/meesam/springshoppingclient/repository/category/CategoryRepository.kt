package com.meesam.springshoppingclient.repository.category

import com.meesam.springshoppingclient.model.CategoryResponse
import retrofit2.Response

interface CategoryRepository {
    suspend fun getAllCategories(): List<CategoryResponse>
}