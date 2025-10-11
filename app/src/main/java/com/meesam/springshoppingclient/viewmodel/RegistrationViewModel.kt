package com.meesam.springshoppingclient.viewmodel

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.model.AuthRegisterRequest
import com.meesam.springshoppingclient.repository.auth.UserAuthRepository
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepository: UserAuthRepository, private val tokenManager: TokenManager) :
    ViewModel() {

    private var _registrationState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val registrationState: StateFlow<AppState<String>> = _registrationState.asStateFlow()

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var emailError by mutableStateOf<String?>(null)

    var passwordError by mutableStateOf<String?>(null)

    var confirmPasswordError by mutableStateOf<String?>(null)

    val isFormValid by derivedStateOf {
        name.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&

                nameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
    }

    fun onEvent(event: UserRegistrationEvents) {
        when (event) {
            is UserRegistrationEvents.OnNameChange -> {
                name = event.name
                isNameValid()
            }

            is UserRegistrationEvents.OnEmailChange -> {
                email = event.email
                isValidEmail()
            }

            is UserRegistrationEvents.OnPasswordChange -> {
                password = event.password
                isPasswordValid()
            }

            is UserRegistrationEvents.OnConfirmPasswordChange -> {
                confirmPassword = event.confirmPassword
                isConfirmPasswordValid()
            }

            is UserRegistrationEvents.OnRegisterClick -> {
                registerUser()
            }
            is UserRegistrationEvents.Reset -> {}
        }
    }

    private fun isValidEmail(): Boolean {
        if (email.isEmpty()) {
            emailError = "Please enter your email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email"
            return false
        }
        emailError = null
        return true
    }

    private fun isNameValid(): Boolean {
        if (name.isEmpty()) {
            nameError = "Please enter your name"
            return false
        }
        if (name.length < 3) {
            nameError = "Name should be greater then 3 character"
            return false
        }
        nameError = null
        return true
    }

    private fun isPasswordValid(): Boolean {
        if (password.isEmpty()) {
            passwordError = "Please enter your password"
            return false
        }
        if (password.length < 3) {
            passwordError = "Password should be greater then 3 character"
            return false
        }
        passwordError = null
        return true
    }

    private fun isConfirmPasswordValid(): Boolean {
        if (confirmPassword == password) {
            confirmPasswordError = null
            return true
        }else{
            confirmPasswordError = "Password didn't match"
            return false
        }
    }

    fun isRegistrationFormValid(): Boolean {
        return isNameValid() && isValidEmail() && isPasswordValid() && isConfirmPasswordValid()
    }

    private fun registerUser() {
        if(isRegistrationFormValid()){
            viewModelScope.launch {
                _registrationState.value = AppState.Loading
                val result = userAuthRepository.register(
                    AuthRegisterRequest(
                        name = name,
                        email = email,
                        password = password
                    )
                )
                if (result.isSuccessful) {
                    tokenManager.saveTempRegisteredEmail(email)
                    _registrationState.value = AppState.Success("You've successfully registered")
                } else {
                    _registrationState.value = AppState.Error("Something went wrong")
                }
            }
        }else {
            return
        }
    }

}