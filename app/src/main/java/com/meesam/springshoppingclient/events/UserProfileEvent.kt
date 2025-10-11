package com.meesam.springshoppingclient.events

sealed class UserProfileEvent {
  data object onSignOut : UserProfileEvent()
  data object onEditClick : UserProfileEvent()
  data object onDismissSheet: UserProfileEvent()
}