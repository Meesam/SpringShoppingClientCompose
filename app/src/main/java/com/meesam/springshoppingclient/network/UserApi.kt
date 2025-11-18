package com.meesam.springshoppingclient.network

import com.meesam.springshoppingclient.model.AddUserAddressRequest
import com.meesam.springshoppingclient.model.ChangePasswordRequest
import com.meesam.springshoppingclient.model.TogglePrimaryAddressRequest
import com.meesam.springshoppingclient.model.UserAddressResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.model.UserUpdateRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApi {

    @GET("user/getUser")
    suspend fun getUserProfile(@Query("id") id: Long): Response<UserResponse>

    @POST("user/changePassword")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<Void>

    @POST("user/update")
    suspend fun updateUserDetails(@Body userUpdateRequest: UserUpdateRequest): Response<UserResponse?>

    @Multipart
    @POST("user/addProfileImage")
    suspend fun uploadProfilePicture(@Part FileItem: MultipartBody.Part, @Part userId: MultipartBody.Part): Response<UserResponse>

    @POST("user/addAddress")
    suspend fun  addAddress(@Body userAddAddressRequest: AddUserAddressRequest): Response<Void>

    @GET("user/getAllAddress")
    suspend fun getUserAddresses(@Query("userId") userId: Long): Response<List<UserAddressResponse>>

    @POST("user/togglePrimaryAddress")
    suspend fun  togglePrimaryAddress(@Body togglePrimaryAddressRequest: TogglePrimaryAddressRequest): Response<Void>

    @DELETE("user/deleteAddress")
    suspend fun  deleteAddress(@Query("addressId") addressId: Long): Response<Void>
}