package com.meesam.springshoppingclient.events

sealed class UserProfileEvent {
  data object OnSignOut : UserProfileEvent()
}