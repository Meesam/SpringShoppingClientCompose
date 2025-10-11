package com.meesam.springshoppingclient.events

import java.time.LocalDateTime

sealed class UserRegistrationEvents {
  data class OnNameChange(val name: String) : UserRegistrationEvents()
  data class OnEmailChange(val email: String) : UserRegistrationEvents()
  data class OnPasswordChange(val password: String) : UserRegistrationEvents()
  data class OnConfirmPasswordChange(val confirmPassword: String) : UserRegistrationEvents()
  data object OnRegisterClick : UserRegistrationEvents()
  data object Reset: UserRegistrationEvents()

}