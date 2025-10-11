package com.meesam.springshoppingclient.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.OtpEvents
import com.meesam.springshoppingclient.model.ActivateUserByOtpRequest
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
class OtpViewModel @Inject constructor(private val userAuthRepository: UserAuthRepository, private val tokenManager: TokenManager) :
    ViewModel() {

    private var _otpState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val otpState: StateFlow<AppState<String>> = _otpState.asStateFlow()

    private var _tempEmail = MutableStateFlow<String>(tokenManager.getTempRegisteredEmail() as String)
    val tempEmail: StateFlow<String?> = _tempEmail.asStateFlow()

    var otp by mutableStateOf("")
        private set

    var otpError by mutableStateOf<String?>(null)
        private set

    val isFormValid by derivedStateOf {
        otp.isNotBlank() && otpError == null
    }

    fun onEvent(events: OtpEvents) {
        when (events) {
            is OtpEvents.OnOtpChange -> {
                otp = events.otp
                isOtpValid()
            }

            OtpEvents.onVerifyClick -> {
                verifyOtp()
            }
        }
    }

    private fun isOtpValid(): Boolean {
        if (otp.isEmpty()) {
            otpError = "Please enter otp"
            return false
        }
        if (otp.length != 6) {
            otpError = "Otp should be 6 digit"
            return false
        }
        otpError = null
        return true
    }

    private fun verifyOtp() {
        if (isOtpValid()) {
            viewModelScope.launch {
                _otpState.value = AppState.Loading
               val result = userAuthRepository.activateUserByOtp(
                    ActivateUserByOtpRequest(
                        otp = otp.toInt(),
                        email = tempEmail.value ?:""
                    )
                )
                if(result.isSuccessful){
                    tokenManager.clearPref()
                    _otpState.value = AppState.Success("Account activated successfully")
                }else {
                    _otpState.value = AppState.Error("Something went wrong")
                }
            }
        }
    }
}