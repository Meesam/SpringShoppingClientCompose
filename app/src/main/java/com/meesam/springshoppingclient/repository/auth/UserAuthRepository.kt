package com.meesam.springshoppingclient.repository.auth

import com.meesam.springshoppingclient.model.*

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthRepository {
    suspend fun login(authLoginRequest: AuthLoginRequest): Response<AuthLoginResponse>
    suspend fun register(authRegisterRequest: AuthRegisterRequest): Response<AuthRegisterResponse>
    suspend fun refreshToken(authRefreshTokenRequest: AuthRefreshTokenRequest): Response<AuthLoginResponse>

    suspend fun logout(authRefreshTokenRequest: AuthRefreshTokenRequest): Response<Void>

    suspend fun activateUserByOtp(activateUserByOtpRequest: ActivateUserByOtpRequest): Response<Void>

    suspend fun generateNewOtp(newOtpRequest: NewOtpRequest): Response<OtpResponse>

    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Response<String>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<OtpResponse>

}