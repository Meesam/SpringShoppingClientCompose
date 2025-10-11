package com.meesam.springshoppingclient.repository.user

import com.meesam.springshoppingclient.model.UserResponse

interface UserRepository {
    suspend fun getUserProfile(id:Long): UserResponse?
}