package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.UserProfileEvent
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.repository.user.UserRepository
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.utils.Constants
import com.meesam.springshoppingclient.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NavigationCommand {
    data class NavigateTo(val route: String) : NavigationCommand()
    object NavigateBack : NavigationCommand()
}



class ProfileViewModel :
    ViewModel() {

    private var _userProfile = MutableStateFlow<AppState<UserResponse?>>(AppState.Loading)
    val userProfile: StateFlow<AppState<UserResponse?>> = _userProfile.asStateFlow()

    private val _navigationCommand = MutableSharedFlow<NavigationCommand>()
    val navigationCommand = _navigationCommand.asSharedFlow()

    private var _isUserLoggedIn = MutableStateFlow<Boolean>(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()


    private val _isLoadingInitialUser = MutableStateFlow(true)
    val isLoadingInitialUser: StateFlow<Boolean> = _isLoadingInitialUser.asStateFlow()

    private val _showEditProfileBottomSheet = MutableStateFlow<Boolean>(false)
    val showEditProfileBottomSheet: StateFlow<Boolean> = _showEditProfileBottomSheet.asStateFlow()

    init {
       // checkIfTokenExist()
       // getUserProfile()
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            UserProfileEvent.onSignOut -> {
              // userLogout()
            }

            UserProfileEvent.onEditClick -> {
                _showEditProfileBottomSheet.value = true
            }

            UserProfileEvent.onDismissSheet -> {
                _showEditProfileBottomSheet.value = false
            }
        }
    }

   /* private fun checkIfTokenExist(){
        _isLoadingInitialUser.value = true
        if(tokenManager.getToken(Constants.ACCESS_TOKEN) != null){
           _isUserLoggedIn.value = true
       }else {
           _isUserLoggedIn.value = false
       }
        _isLoadingInitialUser.value = false
    }*/

    /*fun getUserProfile() {
        _isLoadingInitialUser.value = true
        _userProfile.value = AppState.Loading
        viewModelScope.launch {
            try {
                val userString = tokenManager.getUserDetails()
                val user = Gson().fromJson(userString, UserResponse::class.java)
                //val user = userRepository.getUserProfile(2)
                if (user == null) {
                    _userProfile.value = AppState.Error("Something went wrong")
                } else {
                    _userProfile.value = AppState.Success(user)
                }
            } catch (ex: Exception) {
                _userProfile.value = AppState.Error("Something went wrong")
            } finally {
                _isLoadingInitialUser.value = false
            }

        }
    }*/



    /*fun userLogout() {
        viewModelScope.launch {
            tokenManager.clearPref()
            _userProfile.value = AppState.Success(null)
        }
    }*/
}