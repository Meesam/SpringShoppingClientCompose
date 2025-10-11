package com.meesam.springshoppingclient.network

import com.meesam.springshoppingclient.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("user/getUser")
    suspend fun getUserProfile(@Query("id") id: Long): Response<UserResponse>
}