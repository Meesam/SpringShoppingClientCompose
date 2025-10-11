package com.meesam.springshoppingclient.network


import com.meesam.springshoppingclient.model.ActivateUserByOtpRequest
import com.meesam.springshoppingclient.model.AuthLoginRequest
import com.meesam.springshoppingclient.model.AuthLoginResponse
import com.meesam.springshoppingclient.model.AuthRefreshTokenRequest
import com.meesam.springshoppingclient.model.AuthRegisterRequest
import com.meesam.springshoppingclient.model.AuthRegisterResponse
import com.meesam.springshoppingclient.model.ForgotPasswordRequest
import com.meesam.springshoppingclient.model.NewOtpRequest
import com.meesam.springshoppingclient.model.OtpResponse
import com.meesam.springshoppingclient.model.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body authLoginRequest:AuthLoginRequest): Response<AuthLoginResponse>

    @POST("auth/register")
    suspend fun register(@Body authRegisterRequest: AuthRegisterRequest): Response<AuthRegisterResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body authRefreshTokenRequest: AuthRefreshTokenRequest): Response<AuthLoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Body authRefreshTokenRequest: AuthRefreshTokenRequest): Response<Void>

    @POST("auth/activateUserByOtp")
    suspend fun activateUserByOtp(@Body activateUserByOtpRequest: ActivateUserByOtpRequest): Response<Void>

    @POST("auth/generateNewOtp")
    suspend fun generateNewOtp(@Body newOtpRequest: NewOtpRequest): Response<OtpResponse>

    @POST("auth/forgotPassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<String>

    @POST("auth/resetPassword")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<OtpResponse>

}
