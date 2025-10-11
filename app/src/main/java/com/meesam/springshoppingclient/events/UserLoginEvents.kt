package com.meesam.springshoppingclient.events

sealed class UserLoginEvents {
  data class onEmailChange(val email: String) : UserLoginEvents()
  data class onPasswordChange(val password: String) : UserLoginEvents()
  data object onLoginClick : UserLoginEvents()

  data object reset: UserLoginEvents()
}