package com.meesam.springshoppingclient.events

import com.meesam.springshoppingclient.views.profile.ProfileScreenOptions

sealed class UserProfileEvent {
  data object OnSignOut : UserProfileEvent()
  data object OnDismissSheet: UserProfileEvent()
  data class OnOptionClick(val option: ProfileScreenOptions) : UserProfileEvent()
}