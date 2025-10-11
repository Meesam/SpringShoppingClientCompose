package com.meesam.springshoppingclient.repository.auth

import com.meesam.springshoppingclient.model.*
import com.meesam.springshoppingclient.network.AuthApiService
import jakarta.inject.Inject
import retrofit2.Response

class UserAuthRepositoryImpl @Inject constructor(private val authApiService: AuthApiService): UserAuthRepository {
    override suspend fun login(authLoginRequest: AuthLoginRequest): Response<AuthLoginResponse> {
        return authApiService.login(authLoginRequest)
    }

    override suspend fun register(authRegisterRequest: AuthRegisterRequest): Response<AuthRegisterResponse> {
        return authApiService.register(authRegisterRequest)
    }

    override suspend fun refreshToken(authRefreshTokenRequest: AuthRefreshTokenRequest): Response<AuthLoginResponse> {
        return authApiService.refreshToken(authRefreshTokenRequest)
    }

    override suspend fun logout(authRefreshTokenRequest: AuthRefreshTokenRequest): Response<Void> {
        return authApiService.logout(authRefreshTokenRequest)
    }

    override suspend fun activateUserByOtp(activateUserByOtpRequest: ActivateUserByOtpRequest): Response<Void> {
        return authApiService.activateUserByOtp(activateUserByOtpRequest)
    }

    override suspend fun generateNewOtp(newOtpRequest: NewOtpRequest): Response<OtpResponse> {
        return authApiService.generateNewOtp(newOtpRequest)
    }

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Response<String> {
        return authApiService.forgotPassword(forgotPasswordRequest)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<OtpResponse> {
       return authApiService.resetPassword(resetPasswordRequest)
    }

}