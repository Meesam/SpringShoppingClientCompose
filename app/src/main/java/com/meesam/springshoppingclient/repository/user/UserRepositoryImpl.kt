package com.meesam.springshoppingclient.repository.user

import android.R
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.text.input.rememberTextFieldState
import com.meesam.springshoppingclient.model.AddUserAddressRequest
import com.meesam.springshoppingclient.model.ChangePasswordRequest
import com.meesam.springshoppingclient.model.TogglePrimaryAddressRequest
import com.meesam.springshoppingclient.model.UserAddress
import com.meesam.springshoppingclient.model.UserAddressResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.model.UserUpdateRequest
import com.meesam.springshoppingclient.network.UserApi
import jakarta.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
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

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<Void> {
          return  userApi.changePassword(changePasswordRequest)
    }

    override suspend fun updateUserDetails(userUpdateRequest: UserUpdateRequest): Response<UserResponse?> {
        return userApi.updateUserDetails(userUpdateRequest)
    }

    fun File.toMultipartBodyPart(partName: String, mimeType: String): MultipartBody.Part {
        val requestFile = this.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, this.name, requestFile)
    }

    fun Long.toMultipartBodyPart(partName: String): MultipartBody.Part {
        val requestBody = this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, null, requestBody)
    }

    override suspend fun uploadProfilePicture(
        file: File,
        userId: Long
    ): Response<UserResponse> {
        val filePart = file.toMultipartBodyPart(
            partName = "FileItem",
            mimeType = "image/jpeg"
        )
        val userIdPart = userId.toMultipartBodyPart(
            partName = "userId"
        )
        return userApi.uploadProfilePicture(filePart, userId = userIdPart)
    }

    override suspend fun addAddress(userAddAddressRequest: AddUserAddressRequest): Response<Void> {
        return userApi.addAddress(userAddAddressRequest)
    }

    override suspend fun getUserAddresses(userId: Long): Response<List<UserAddressResponse>> {
        return userApi.getUserAddresses(userId = userId)
    }

    override suspend fun togglePrimaryAddress(togglePrimaryAddressRequest: TogglePrimaryAddressRequest): Response<Void> {
        return userApi.togglePrimaryAddress(togglePrimaryAddressRequest = togglePrimaryAddressRequest)
    }

}