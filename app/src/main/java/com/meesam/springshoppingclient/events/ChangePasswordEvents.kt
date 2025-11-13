package com.meesam.springshoppingclient.events

sealed class ChangePasswordEvents {
    data object OnChangePasswordButtonClick : ChangePasswordEvents()

}