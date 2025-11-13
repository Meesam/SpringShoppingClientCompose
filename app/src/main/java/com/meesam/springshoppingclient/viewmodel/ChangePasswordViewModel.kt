package com.meesam.springshoppingclient.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.ChangePasswordEvents
import com.meesam.springshoppingclient.model.ChangePasswordRequest
import com.meesam.springshoppingclient.model.ErrorResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.repository.user.UserRepository
import com.meesam.springshoppingclient.states.AppState
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
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private var _changePasswordState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val changePasswordState: StateFlow<AppState<String>> = _changePasswordState.asStateFlow()

    var newPassword = TextFieldState()
    var oldPassword = TextFieldState()
    var newPasswordError by mutableStateOf<String?>(null)
    var oldPasswordError by mutableStateOf<String?>(null)

    init {
        snapshotFlow { newPassword.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPasswordValid()
            }.launchIn(viewModelScope)

        snapshotFlow { oldPassword.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPasswordValid()
            }.launchIn(viewModelScope)
    }

    val isFormValid by derivedStateOf {
        newPassword.text.isNotBlank() &&
                oldPassword.text.isNotBlank() &&
                newPasswordError == null &&
                oldPasswordError == null
    }

    private fun isPasswordValid(): Boolean {
        if (newPassword.text.isEmpty()) {
            newPasswordError = "Please enter your password"
            return false
        }
        if (newPassword.text.length < 3) {
            newPasswordError = "Password should be greater then 3 character"
            return false
        }
        newPasswordError = null
        return true
    }

    private fun isOldPasswordValid(): Boolean {
        if (oldPassword.text.isEmpty()) {
            oldPasswordError = "Please enter your password"
            return false
        }
        if (oldPassword.text.length < 3) {
            oldPasswordError = "Password should be greater then 3 character"
            return false
        }
        newPasswordError = null
        return true
    }

    fun isChangePasswordFormValid(): Boolean {
        return isPasswordValid() && isOldPasswordValid()
    }

    fun onEvent(event: ChangePasswordEvents) {
        when (event) {
            is ChangePasswordEvents.OnChangePasswordButtonClick -> {
                changePassword()
            }
        }
    }

    private fun changePassword() {
        if (isChangePasswordFormValid()) {
            viewModelScope.launch {
                _changePasswordState.value = AppState.Loading
                try {
                    val userString = tokenManager.getUserDetails()
                    val user = Gson().fromJson(userString, UserResponse::class.java)
                    val changePasswordRequest = ChangePasswordRequest(
                        newPassword = newPassword.text.toString(),
                        oldPassword = oldPassword.text.toString(),
                        email = user.email
                    )
                     val result = userRepository.changePassword(changePasswordRequest)
                    if(result.isSuccessful){
                        _changePasswordState.value = AppState.Success("Password changed successfully, Please logout and login with new password.")
                    }else {
                        val gson = Gson()
                        val err = result.errorBody()?.string()
                        val errorResponse = gson.fromJson(err, ErrorResponse::class.java)
                        _changePasswordState.value = AppState.Error(err?.isEmpty()?.let { if(!it) errorResponse.message else  "Something went wrong." })
                    }

                } catch (ex: Exception) {
                    _changePasswordState.value = AppState.Error(ex.message.toString())
                }
            }
        } else {
            return
        }

    }
}