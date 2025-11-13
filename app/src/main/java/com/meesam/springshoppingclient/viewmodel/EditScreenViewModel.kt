package com.meesam.springshoppingclient.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.meesam.springshoppingclient.events.EditProfileEvents
import com.meesam.springshoppingclient.events.UserRegistrationEvents
import com.meesam.springshoppingclient.model.ErrorResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.model.UserUpdateRequest
import com.meesam.springshoppingclient.pref.ACCESS_TOKEN_KEY
import com.meesam.springshoppingclient.pref.REFRESH_TOKEN_KEY
import com.meesam.springshoppingclient.pref.USER_DETAILS_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import com.meesam.springshoppingclient.repository.user.UserRepository
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(FlowPreview::class)
@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private var _editProfileState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val editProfileState: StateFlow<AppState<String>> = _editProfileState.asStateFlow()

    private val _profilePictureLoading = MutableStateFlow(false)
    val profilePictureLoading: StateFlow<Boolean> = _profilePictureLoading.asStateFlow()

    private var _userProfile = MutableStateFlow<AppState<UserResponse?>>(AppState.Loading)
    val userProfile: StateFlow<AppState<UserResponse?>> = _userProfile.asStateFlow()

    val name = TextFieldState()
    val phone = TextFieldState()
    val dob = TextFieldState(initialText = "")

    private var _profilePicture = MutableStateFlow<String>("")
    val profilePicture: StateFlow<String> = _profilePicture.asStateFlow()


    var nameError by mutableStateOf<String?>(null)
        private set

    var phoneError by mutableStateOf<String?>(null)
        private set

    var dobError by mutableStateOf<String?>(null)
    val userDetailsFromPreferences = userPreferences.getPref(key = USER_DETAILS_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    init {
        getUserProfile()
        // Observe changes to the name field's text
        snapshotFlow { name.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isNameValid()
            }.launchIn(viewModelScope)

        snapshotFlow { phone.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isPhoneValid()
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

    private fun isPhoneValid(): Boolean {
        if(phone.text ==""){
            phoneError = null
            return true
        }
        if (phone.text.length != 10) {
            phoneError = "Mobile number should be 10 digit"
            return false
        }
        phoneError = null
        return true
    }

    fun isEditProfileFormValid(): Boolean {
        return isNameValid() && isValidDob()
    }

    fun onEvent(event: EditProfileEvents) {
        when (event) {
            is EditProfileEvents.OnSaveChangesClick -> {
                saveChanges()
            }

            is EditProfileEvents.OnTackPictureClick -> {
                uploadProfilePicture(profilePicture = event.profilePicture)
            }
        }
    }

    private fun uploadProfilePicture(profilePicture: File,) {
        _profilePictureLoading.value = true
        viewModelScope.launch {
            val userString = userDetailsFromPreferences.value
            val user = Gson().fromJson(userString, UserResponse::class.java)
            try {
                val result = userRepository.uploadProfilePicture(userId = user.id.toLong(), file = profilePicture)
                if (result.isSuccessful) {
                    result.body()?.let {
                        val userString = Gson().toJson(it)
                        userPreferences.savePref(key = USER_DETAILS_KEY, pref = userString)
                    }
                } else {
                    val gson = Gson()
                    val err = result.errorBody()?.string()
                    val errorResponse = gson.fromJson(err, ErrorResponse::class.java)
                    Log.d("PICTUREAPI",errorResponse.message )
                    //_loginUiState.value = AppState.Error(
                    //err?.isEmpty()
                    //  ?.let { if (!it) errorResponse.message else "Something went wrong" })
                }
            }catch (ex: Exception){
                Log.d("PICTUREAPI EX",ex.message.toString() )
            }

        }
    }

    /*fun getUserProfile() {
        _userProfile.value = AppState.Loading
        viewModelScope.launch {
            try {
                val userString = tokenManager.getUserDetails()
                val user = Gson().fromJson(userString, UserResponse::class.java)
                if (user == null) {
                    _userProfile.value = AppState.Error("Something went wrong")
                } else {
                   name.edit {
                       replace(0,length, user.name)
                   }
                    dob.edit {
                        replace(0,length, user.dob)
                    }
                }
            } catch (ex: Exception) {
                _userProfile.value = AppState.Error("Something went wrong")
            }
        }
    }*/

    private fun getUserProfile() {
        _userProfile.value = AppState.Loading
        viewModelScope.launch {
            try {
                userDetailsFromPreferences.collect { userDetailsString ->
                    if (userDetailsString.isNotEmpty()) {
                        val user = Gson().fromJson(userDetailsString, UserResponse::class.java)
                        user?.let {
                            name.edit {
                                replace(0,length, user.name)
                            }
                            dob.edit {
                                replace(0,length, user.dob)
                            }
                            phone.edit {
                                replace(0,length,user.phone.toString())
                            }
                            _profilePicture.value = user.profilePicUrl.toString()
                        } ?: {
                            _userProfile.value = AppState.Error("Something went wrong")
                        }
                    } else {
                        _userProfile.value = AppState.Loading
                    }
                }
            } catch (ex: Exception) {
                _userProfile.value = AppState.Error("Something went wrong")
            } finally {
            }
        }
    }

    private fun convertStringToKotlinxLocalDate(dateString: String): LocalDate? {
        return try {
            if (dateString.length == 10 && dateString[4] == '-' && dateString[7] == '-') {
                LocalDate.parse(dateString)
            } else {
                null
            }
        }catch (ex: Exception){
            null
        }
    }

    private fun saveChanges() {
        if (isEditProfileFormValid()) {
            viewModelScope.launch {
                _editProfileState.value = AppState.Loading
                val dateOfBirth = convertStringToKotlinxLocalDate(dob.text.toString()).toString()
                val userString = userDetailsFromPreferences.value
                val user = Gson().fromJson(userString, UserResponse::class.java)
                val updateUserUpdateRequest = UserUpdateRequest(
                    id = user.id.toLong(),
                    name = name.text.toString(),
                    dob = dateOfBirth,
                    phone = phone.text.toString()
                )
                val result = userRepository.updateUserDetails(updateUserUpdateRequest)
                if (result.isSuccessful) {
                    result.body()?.let {
                        val userString = Gson().toJson(it)
                        userPreferences.savePref(key = USER_DETAILS_KEY, pref = userString)
                        _editProfileState.value = AppState.Success("User details updated successfully")
                    }

                } else {
                    val gson = Gson()
                    val err = result.errorBody()?.string()
                    val errorResponse = gson.fromJson(err, ErrorResponse::class.java)
                    _editProfileState.value = AppState.Error(
                        err?.isEmpty()
                            ?.let { if (!it) errorResponse.message else "Something went wrong" })
                }
            }
        } else return
    }
}