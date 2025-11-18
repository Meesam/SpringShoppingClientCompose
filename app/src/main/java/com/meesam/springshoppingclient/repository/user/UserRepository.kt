package com.meesam.springshoppingclient.repository.user

import android.R
import android.content.Context
import android.net.Uri
import com.meesam.springshoppingclient.model.AddUserAddressRequest
import com.meesam.springshoppingclient.model.ChangePasswordRequest
import com.meesam.springshoppingclient.model.TogglePrimaryAddressRequest
import com.meesam.springshoppingclient.model.UserAddress
import com.meesam.springshoppingclient.model.UserAddressResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.model.UserUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.Query
import java.io.File

interface UserRepository {
    suspend fun getUserProfile(id:Long): UserResponse?

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<Void>

    suspend fun updateUserDetails(userUpdateRequest: UserUpdateRequest): Response<UserResponse?>

    suspend fun uploadProfilePicture(file: File, userId:Long): Response<UserResponse>

    suspend fun  addAddress(userAddAddressRequest: AddUserAddressRequest): Response<Void>

    suspend fun getUserAddresses(userId: Long): Response<List<UserAddressResponse>>

    suspend fun  togglePrimaryAddress(togglePrimaryAddressRequest: TogglePrimaryAddressRequest): Response<Void>

    suspend fun  deleteAddress(addressId: Long): Response<Void>
}