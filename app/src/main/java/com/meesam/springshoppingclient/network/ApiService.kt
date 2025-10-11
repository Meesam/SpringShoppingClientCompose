package com.meesam.springshoppingclient.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/*interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    @POST("users")
    suspend fun createUser(@Body user: UserRequest): Response<UserResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Long)

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserResponse>

    @GET("users/{username}")
    suspend fun getUserByName(@Path("username") id: String): Response<UserResponse>
}*/