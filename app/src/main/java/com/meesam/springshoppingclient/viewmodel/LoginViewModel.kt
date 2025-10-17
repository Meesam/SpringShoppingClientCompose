package com.meesam.springshoppingclient.viewmodel

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.UserLoginEvents
import com.meesam.springshoppingclient.model.AuthLoginRequest
import com.meesam.springshoppingclient.model.ErrorResponse
import com.meesam.springshoppingclient.repository.auth.UserAuthRepository
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val loginUiState: StateFlow<AppState<String>> = _loginUiState.asStateFlow()

    val email = TextFieldState()
    val password = TextFieldState()

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set


    init {
        _loginUiState.value = AppState.Idle
        // Observe changes to the email field's text
        snapshotFlow { email.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isEmailValid()
            }.launchIn(viewModelScope)

        snapshotFlow { password.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPasswordValid()
            }.launchIn(viewModelScope)

    }


    fun onEvent(event: UserLoginEvents) {
        when (event) {
            is UserLoginEvents.OnLoginClick -> {
                signInUser()
            }

            is UserLoginEvents.Reset -> {
                reset()
            }
        }
    }

    private fun isEmailValid(): Boolean {
        if (email.text.isBlank()) {
            emailError = "Email cannot be empty"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            emailError = "Invalid email address"
            return false
        }
        emailError = null
        return true
    }

    private fun isPasswordValid(): Boolean {
        if (password.text.isEmpty()) {
            passwordError = "Please enter your password"
            return false
        }
        if (password.text.length < 3) {
            passwordError = "Password should be greater then 3 character"
            return false
        }
        passwordError = null
        return true
    }

    val isFormValid by derivedStateOf {
        email.text.isNotBlank() &&
                password.text.isNotBlank() &&
                emailError == null &&
                passwordError == null
    }

    private fun isLoginFormValid(): Boolean {
        val emailValid = isEmailValid()
        val passwordValid = isPasswordValid()
        return emailValid && passwordValid
    }

    private fun signInUser() {
        if (!isLoginFormValid()) {
            return
        }
        viewModelScope.launch {
            _loginUiState.value = AppState.Loading
            val request = AuthLoginRequest(
                email = email.text.toString(),
                password = password.text.toString()
            )
            try{
                val result = userAuthRepository.login(request)
                if (result.isSuccessful) {
                    result.body()?.let {
                        tokenManager.saveToken(it.accessToken, Constants.ACCESS_TOKEN)
                        tokenManager.saveToken(it.refreshToken, Constants.REFRESH_TOKEN)
                        val userString = Gson().toJson(it.user)
                        tokenManager.saveUserDetail(userString)
                        _loginUiState.value = AppState.Success("Success")
                    }
                } else {
                    val gson = Gson()
                    val err = result.errorBody()?.string()
                    val errorResponse = gson.fromJson(err, ErrorResponse::class.java)
                    _loginUiState.value = AppState.Error(err?.isEmpty()?.let { if(!it) errorResponse.message else  "Something went wrong" })
                }
            }catch (ex: Exception){
                _loginUiState.value = AppState.Error("Something went wrong")
            }

        }
    }

    private fun reset() {
        emailError = null
        passwordError = null
        _loginUiState.value = AppState.Idle
    }

}