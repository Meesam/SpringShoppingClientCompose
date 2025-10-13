package com.meesam.springshoppingclient.events

import java.time.LocalDateTime

sealed class UserRegistrationEvents {
  data object OnRegisterClick : UserRegistrationEvents()
  data object Reset: UserRegistrationEvents()

}