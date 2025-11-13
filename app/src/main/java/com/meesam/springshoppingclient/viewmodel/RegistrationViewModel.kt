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
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.model.AuthRegisterRequest
import com.meesam.springshoppingclient.model.ErrorResponse
import com.meesam.springshoppingclient.pref.TEMP_EMAIL_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import com.meesam.springshoppingclient.repository.auth.UserAuthRepository
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
    private val userPref: UserPreferences
) :
    ViewModel() {

    private var _registrationState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val registrationState: StateFlow<AppState<String>> = _registrationState.asStateFlow()

    val name = TextFieldState()
    val email = TextFieldState()
    val phone = TextFieldState()
    var password = TextFieldState()

    var confirmPassword = TextFieldState()

    var nameError by mutableStateOf<String?>(null)
        private set

    var emailError by mutableStateOf<String?>(null)

    var passwordError by mutableStateOf<String?>(null)

    var confirmPasswordError by mutableStateOf<String?>(null)

    var phoneError by mutableStateOf<String?>(null)

    init {
        // Observe changes to the name field's text
        snapshotFlow { name.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isNameValid()
            }.launchIn(viewModelScope)

        // Observe changes to the email field's text
        snapshotFlow { email.text }
            .drop(1)
            .debounce(300) // This prevents validating on every single keystroke
            .onEach {
                isValidEmail()
            }.launchIn(viewModelScope)

        snapshotFlow { password.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPasswordValid()
            }.launchIn(viewModelScope)

        snapshotFlow { phone.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPhoneValid()
            }.launchIn(viewModelScope)

        snapshotFlow { confirmPassword.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isConfirmPasswordValid()
            }.launchIn(viewModelScope)
    }

    val isFormValid by derivedStateOf {
        name.text.isNotBlank() &&
                email.text.isNotBlank() &&
                password.text.isNotBlank() &&
                confirmPassword.text == password.text &&
                nameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
    }


    fun onEvent(event: UserRegistrationEvents) {
        when (event) {
            is UserRegistrationEvents.OnRegisterClick -> {
                registerUser()
            }

            is UserRegistrationEvents.Reset -> {}
        }
    }

    private fun isValidEmail(): Boolean {
        if (email.text.isEmpty()) {
            emailError = "Please enter your email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            emailError = "Please enter a valid email"
            return false
        }
        emailError = null
        return true
    }

    private fun isNameValid(): Boolean {
        if (name.text.isEmpty()) {
            nameError = "Please enter your name"
            return false
        }
        if (name.text.length < 3) {
            nameError = "Name should be greater then 3 character"
            return false
        }
        nameError = null
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

    private fun isPhoneValid(): Boolean {
        if(phone.text ==""){
            phoneError = null
            return true
        }
        if (phone.text.length != 10) {
            phoneError = "Mobile number should be 10 digit"
            return false
        }
        phoneError = null
        return true
    }

    private fun isConfirmPasswordValid(): Boolean {
        if (confirmPassword.text.toString() != password.text.toString()) {
            confirmPasswordError = "Password didn't match"
            return false
        }
        confirmPasswordError = null
        return true
    }

    fun isRegistrationFormValid(): Boolean {
        return isNameValid() && isValidEmail() && isPasswordValid() && isConfirmPasswordValid()
    }

    private fun registerUser() {
        if (isRegistrationFormValid()) {
            viewModelScope.launch {
                _registrationState.value = AppState.Loading
                try {
                    val result = userAuthRepository.register(
                        AuthRegisterRequest(
                            name = name.text.toString(),
                            email = email.text.toString(),
                            password = password.text.toString()
                        )
                    )
                    if (result.isSuccessful) {
                        userPref.savePref(pref = email.text.toString(), key = TEMP_EMAIL_KEY)
                        _registrationState.value =
                            AppState.Success("You've successfully registered")
                    } else {
                        val gson = Gson()
                        val err = result.errorBody()?.string()
                        val errorResponse = gson.fromJson(err, ErrorResponse::class.java)
                        _registrationState.value = AppState.Error(
                            err?.isEmpty()
                                ?.let { if (!it) errorResponse.message else "Something went wrong" })
                    }
                } catch (ex: Exception) {
                    _registrationState.value = AppState.Error(ex.message.toString())
                }
            }
        } else {
            return
        }
    }

}