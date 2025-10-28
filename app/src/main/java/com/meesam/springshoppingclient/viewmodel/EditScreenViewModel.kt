package com.meesam.springshoppingclient.viewmodel

import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
@HiltViewModel
class EditScreenViewModel @Inject constructor(): ViewModel() {
    private var _editProfileState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val editProfileState: StateFlow<AppState<String>> = _editProfileState.asStateFlow()

    val name = TextFieldState()
    val dob = TextFieldState(initialText = "")
    var nameError by mutableStateOf<String?>(null)
        private set

    var dobError by mutableStateOf<String?>(null)

    init {
        // Observe changes to the name field's text
        snapshotFlow { name.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isNameValid()
            }.launchIn(viewModelScope)

        // Observe changes to the email field's text
        snapshotFlow { dob.text }
            .drop(1)
            .debounce(300) // This prevents validating on every single keystroke
            .onEach {
                isValidDob()
            }.launchIn(viewModelScope)

    }

    val isFormValid by derivedStateOf {
        name.text.isNotBlank() &&
                dob.text.isNotBlank() &&
                nameError == null &&
                dobError == null
    }

    private fun isValidDob(): Boolean {
        if (dob.text.isEmpty()) {
            dobError = "Please enter your dob"
            return false
        }
        dobError = null
        return true
    }

    private fun isNameValid(): Boolean {
        if (name.text.isEmpty()) {
            nameError = "Please enter your name"
            return false
        }
        if (name.text.length < 3) {
            nameError = "Name should be greater then 3 character"
            return false
        }
        nameError = null
        return true
    }

    fun isEditProfileFormValid(): Boolean {
        return isNameValid() && isValidDob()
    }

    fun onEvent(event: EditProfileEvents) {
        when (event) {
            is EditProfileEvents.OnSaveChangesClick -> {
                //registerUser()
            }
            is EditProfileEvents.OnCancelClick -> {}
        }
    }
}