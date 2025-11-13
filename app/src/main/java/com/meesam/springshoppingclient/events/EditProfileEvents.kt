package com.meesam.springshoppingclient.events

import java.io.File

sealed class EditProfileEvents {
    data object OnSaveChangesClick : EditProfileEvents()
    data class OnTackPictureClick(val profilePicture: File): EditProfileEvents()
}