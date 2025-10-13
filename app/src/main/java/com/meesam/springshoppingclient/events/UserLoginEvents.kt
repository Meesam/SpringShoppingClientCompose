package com.meesam.springshoppingclient.events

sealed class UserLoginEvents {
  data object OnLoginClick : UserLoginEvents()
  data object Reset: UserLoginEvents()
}