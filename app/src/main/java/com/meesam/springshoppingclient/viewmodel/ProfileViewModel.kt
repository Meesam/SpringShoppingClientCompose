package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.pref.ACCESS_TOKEN_KEY
import com.meesam.springshoppingclient.pref.USER_DETAILS_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NavigationCommand {
    data class NavigateTo(val route: String) : NavigationCommand()
    object NavigateBack : NavigationCommand()
}


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) :
    ViewModel() {

    private var _userProfile = MutableStateFlow<AppState<UserResponse?>>(AppState.Loading)
    val userProfile: StateFlow<AppState<UserResponse?>> = _userProfile.asStateFlow()

    private var _isUserLoggedIn = MutableStateFlow<Boolean>(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()


    private val _isLoadingInitialUser = MutableStateFlow(true)
    val isLoadingInitialUser: StateFlow<Boolean> = _isLoadingInitialUser.asStateFlow()

    val accessTokenFromPreferences = userPreferences.getPref(key = ACCESS_TOKEN_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    val userDetailsFromPreferences = userPreferences.getPref(key = USER_DETAILS_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    init {
        checkIfTokenExist()
        getUserProfile()
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.OnSignOut -> {
                userLogout()
            }
        }
    }

    private fun checkIfTokenExist() {
        _isLoadingInitialUser.value = true
        try {
            viewModelScope.launch {
                accessTokenFromPreferences.collect {
                    _isUserLoggedIn.value = it.isNotEmpty()
                }
            }
        } catch (_: Exception) {
            _isUserLoggedIn.value = false
        } finally {
            _isLoadingInitialUser.value = false
        }

    }

    fun getUserProfile() {
        _isLoadingInitialUser.value = true
        _userProfile.value = AppState.Loading
        viewModelScope.launch {
            try {
                userDetailsFromPreferences.collect { userDetailsString ->
                    if (userDetailsString.isNotEmpty()) {
                        val user = Gson().fromJson(userDetailsString, UserResponse::class.java)
                        user?.let {
                            _userProfile.value = AppState.Success(user)
                        } ?: {
                            _userProfile.value = AppState.Error("Something went wrong")
                        }
                    } else {
                        _userProfile.value = AppState.Loading
                    }
                    _isLoadingInitialUser.value = false
                }
            } catch (_: Exception) {
                _userProfile.value = AppState.Error("Something went wrong")
                _isLoadingInitialUser.value = false
            } finally {
                _isLoadingInitialUser.value = false
            }
        }
    }


    fun userLogout() {
        viewModelScope.launch {
            userPreferences.clear()
        }
    }
}