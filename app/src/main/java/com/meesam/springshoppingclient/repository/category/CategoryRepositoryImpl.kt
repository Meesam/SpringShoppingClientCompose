package com.meesam.springshoppingclient.repository.category


import com.meesam.springshoppingclient.model.CategoryResponse
import com.meesam.springshoppingclient.network.CategoryApi
import jakarta.inject.Inject
import retrofit2.Response

class CategoryRepositoryImpl @Inject constructor(private val categoryApi: CategoryApi):
    CategoryRepository {
    override suspend fun getAllCategories(): List<CategoryResponse> {
        val response = categoryApi.getAllCategory()
        if(response.isSuccessful && response.body() !=null){
            return response.body()!!
        }
        return emptyList()
    }

}