package com.meesam.springshoppingclient.repository.user

import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.network.UserApi
import jakarta.inject.Inject
import java.lang.Exception

class UserRepositoryImpl @Inject constructor(private val userApi: UserApi): UserRepository {
    override suspend fun getUserProfile(id: Long): UserResponse? {
        return try {
            val response = userApi.getUserProfile(id)
            if(response.isSuccessful && response.body() !=null){
                response.body()
            }else{
                null
            }
        }catch (ex: Exception){
            null
        }
    }
}