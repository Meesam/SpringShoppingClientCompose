package com.meesam.springshoppingclient.events

sealed class EditProfileEvents {
    data object OnSaveChangesClick : EditProfileEvents()
    data object OnCancelClick: EditProfileEvents()
}