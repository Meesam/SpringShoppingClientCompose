package com.meesam.springshoppingclient.viewmodel


import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.AddressEvents
import com.meesam.springshoppingclient.model.AddUserAddressRequest
import com.meesam.springshoppingclient.model.ErrorResponse
import com.meesam.springshoppingclient.model.TogglePrimaryAddressRequest
import com.meesam.springshoppingclient.model.UserAddressResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.pref.USER_DETAILS_KEY
import com.meesam.springshoppingclient.pref.UserPreferences
import com.meesam.springshoppingclient.repository.user.UserRepository
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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


data class AddressType(val title: String, val icon: ImageVector, val isSelected: Boolean)

@OptIn(FlowPreview::class)
@HiltViewModel
class AddressViewModel @Inject constructor(
    private var userRepository: UserRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {


    val userDetailsFromPreferences = userPreferences.getPref(key = USER_DETAILS_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )
    private var _addressTypeState = MutableStateFlow<AppState<List<AddressType>>>(AppState.Loading)
    val addressTypeState: StateFlow<AppState<List<AddressType>>> = _addressTypeState.asStateFlow()

    private var _addAddressState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val addAddressState: StateFlow<AppState<String>> = _addAddressState.asStateFlow()

    private var _userAddress =
        MutableStateFlow<AppState<List<UserAddressResponse>>>(AppState.Loading)
    val userAddress: StateFlow<AppState<List<UserAddressResponse>>> = _userAddress.asStateFlow()

    private var _selectedAddressType = MutableStateFlow("Home")
    val selectedAddressType: StateFlow<String> = _selectedAddressType.asStateFlow()

    private var _deleteAddressState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val deleteAddressState: StateFlow<AppState<String>> = _deleteAddressState.asStateFlow()

    val street = TextFieldState()
    val zipCode = TextFieldState()
    val contactPerson = TextFieldState()
    val contactNumber = TextFieldState()
    val nearBy = TextFieldState()

    var streetError by mutableStateOf<String?>(null)
        private set

    var zipCodeError by mutableStateOf<String?>(null)
        private set

    var contactPersonError by mutableStateOf<String?>(null)
        private set

    var contactNumberError by mutableStateOf<String?>(null)
        private set

    init {
        // Observe changes to the name field's text
        snapshotFlow { contactPerson.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isNameValid()
            }.launchIn(viewModelScope)

        // Observe changes to the email field's text
        snapshotFlow { contactNumber.text }
            .drop(1)
            .debounce(300) // This prevents validating on every single keystroke
            .onEach {
                isPhoneValid()
            }.launchIn(viewModelScope)

        snapshotFlow { street.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isStreetValid()
            }.launchIn(viewModelScope)

        snapshotFlow { zipCode.text }
            .drop(1)
            .debounce(300)
            .onEach {
                isZipCodeValid()
            }.launchIn(viewModelScope)

    }

    init {
        loadAccountType()
        getUserAddress()
    }

    val addressTypes = listOf(
        AddressType(title = "Home", icon = Icons.Filled.Home, isSelected = true),
        AddressType(title = "Hotel", icon = Icons.Filled.Hotel, isSelected = false),
        AddressType(title = "Work", icon = Icons.Filled.Apartment, isSelected = false)
    )

    private var _togglePrimaryAddressState = MutableStateFlow<AppState<String>>(AppState.Idle)
    val togglePrimaryAddressState: StateFlow<AppState<String>> =
        _togglePrimaryAddressState.asStateFlow()

    private fun getUserAddress() {
        viewModelScope.launch {
            _userAddress.value = AppState.Loading
            userDetailsFromPreferences.collect { userDetailsString ->
                if (userDetailsString.isNotEmpty()) {
                    val user = Gson().fromJson(userDetailsString, UserResponse::class.java)
                    user?.let { userDetails ->
                        val result =
                            userRepository.getUserAddresses(userId = userDetails.id.toLong())
                        if (result.isSuccessful) {
                            _userAddress.value = AppState.Success(result.body()!!)
                        } else {
                            val gson = Gson()
                            val err = result.errorBody()?.string()
                            val errorResponse =
                                gson.fromJson(err, ErrorResponse::class.java)
                            _userAddress.value = AppState.Error(
                                err?.isEmpty()
                                    ?.let { if (!it) errorResponse.message else "Something went wrong" })
                        }
                    } ?: {
                        _userAddress.value = AppState.Error("Something went wrong")
                    }
                } else {
                    _userAddress.value = AppState.Loading
                }
            }
        }
    }


    private fun updateAddressInList(id: Long) {
        val currentState = _userAddress.value
        if (currentState is AppState.Success) {
            val currentList = currentState.data
            val newList = currentList.map { existingAddress ->
                if (existingAddress.id == id) {
                    existingAddress.copy(isPrimary = true)
                } else {
                    existingAddress.copy(isPrimary = false)
                }
            }
            _userAddress.value = AppState.Success(newList)
        }
    }

    private fun loadAccountType() {
        viewModelScope.launch {
            _addressTypeState.value = AppState.Loading
            delay(2000)
            _addressTypeState.value = AppState.Success(addressTypes)
        }
    }

    fun onEvent(events: AddressEvents) {
        when (events) {
            is AddressEvents.OnSelectAddressType -> {
                val updatedAddressTypeList = addressTypes.map {
                    if (it.title == events.addressType) {
                        _selectedAddressType.value = events.addressType
                        it.copy(isSelected = true)
                    } else {
                        it.copy(isSelected = false)
                    }
                }
                _addressTypeState.value = AppState.Success(updatedAddressTypeList)
            }

            is AddressEvents.OnSaveChangesClick -> {
                saveAddress()
            }

            is AddressEvents.OnAddressToggleClick -> {
                viewModelScope.launch {
                    _togglePrimaryAddressState.value = AppState.Loading
                    userDetailsFromPreferences.collect { userDetailsString ->
                        if (userDetailsString.isNotEmpty()) {
                            val user = Gson().fromJson(userDetailsString, UserResponse::class.java)
                            user?.let { userDetails ->
                                val togglePrimaryAddressRequest = TogglePrimaryAddressRequest(
                                    userId = userDetails.id.toLong(),
                                    addressId = events.addressId
                                )
                                val result =
                                    userRepository.togglePrimaryAddress(togglePrimaryAddressRequest)
                                if (result.isSuccessful) {
                                    updateAddressInList(events.addressId)
                                    _togglePrimaryAddressState.value =
                                        AppState.Success("Address added successfully")
                                } else {
                                    val gson = Gson()
                                    val err = result.errorBody()?.string()
                                    val errorResponse =
                                        gson.fromJson(err, ErrorResponse::class.java)
                                    _togglePrimaryAddressState.value = AppState.Error(
                                        err?.isEmpty()
                                            ?.let { if (!it) errorResponse.message else "Something went wrong" })
                                }
                            } ?: {
                                _togglePrimaryAddressState.value =
                                    AppState.Error("Something went wrong")
                            }
                        } else {
                            _togglePrimaryAddressState.value =
                                AppState.Error("Something went wrong")
                        }
                    }
                }
            }

            is AddressEvents.LoadUserAddressList -> {
                getUserAddress()
            }

            is AddressEvents.OnDeleteAddressClick -> {
                deleteUserAddress(events.addressId)
            }

            is AddressEvents.OnResetState -> {
                resetState()
            }
        }
    }

    private fun deleteUserAddress(addressId: Long) {
        viewModelScope.launch {
            _deleteAddressState.value = AppState.Loading
            try {
                val result = userRepository.deleteAddress(addressId)
                if (result.isSuccessful) {
                    _deleteAddressState.value = AppState.Success("Address deleted successfully")
                } else {
                    _deleteAddressState.value =
                        AppState.Error("Something went wrong while deleting address")
                }
            } catch (ex: Exception) {
                _deleteAddressState.value = AppState.Error(ex.message.toString())
            }
        }
    }

    val isFormValid by derivedStateOf {
        contactPerson.text.isNotBlank() &&
                contactNumber.text.isNotBlank() &&
                street.text.isNotBlank() &&
                zipCode.text.isNotBlank() &&
                contactPersonError == null &&
                contactNumberError == null &&
                streetError == null &&
                zipCodeError == null
    }

    private fun isPhoneValid(): Boolean {
        if (contactNumber.text == "") {
            contactNumberError = null
            return true
        }
        if (contactNumber.text.length != 10) {
            contactNumberError = "Mobile number should be 10 digit"
            return false
        }
        contactNumberError = null
        return true
    }

    private fun isNameValid(): Boolean {
        if (contactPerson.text.isEmpty()) {
            contactPersonError = "Please enter name"
            return false
        }
        if (contactPerson.text.length < 3) {
            contactPersonError = "Name should be greater then 3 character"
            return false
        }
        contactPersonError = null
        return true
    }

    private fun isStreetValid(): Boolean {
        if (street.text.isEmpty()) {
            streetError = "Please enter street name"
            return false
        }
        if (street.text.length < 3) {
            streetError = "Street name should be greater then 3 character"
            return false
        }
        streetError = null
        return true
    }

    private fun isZipCodeValid(): Boolean {
        if (zipCode.text.isEmpty()) {
            zipCodeError = "Please enter zip code"
            return false
        }
        if (zipCode.text.length < 3) {
            zipCodeError = "Zip code should be greater then 3 character"
            return false
        }
        zipCodeError = null
        return true
    }

    fun isAddressFormValid(): Boolean {
        return isNameValid() && isPhoneValid() && isStreetValid() && isZipCodeValid()
    }

    private fun saveAddress() {
        if (isAddressFormValid()) {
            viewModelScope.launch {
                try {
                    _addAddressState.value = AppState.Loading
                    userDetailsFromPreferences.collect { userDetailsString ->
                        if (userDetailsString.isNotEmpty()) {
                            val user = Gson().fromJson(userDetailsString, UserResponse::class.java)
                            user?.let { userDetails ->
                                val userAddUserAddressRequest = AddUserAddressRequest(
                                    userId = userDetails.id.toLong(),
                                    street = street.text.toString(),
                                    zipCode = zipCode.text.toString(),
                                    contactName = contactPerson.text.toString(),
                                    contactNumber = contactNumber.text.toString(),
                                    nearBy = nearBy.text.toString(),
                                    addressType = selectedAddressType.value
                                )
                                val result = userRepository.addAddress(userAddUserAddressRequest)
                                if (result.isSuccessful) {
                                    _addAddressState.value =
                                        AppState.Success("Address added successfully")
                                    getUserAddress()

                                } else {
                                    val gson = Gson()
                                    val err = result.errorBody()?.string()
                                    val errorResponse =
                                        gson.fromJson(err, ErrorResponse::class.java)
                                    _addAddressState.value = AppState.Error(
                                        err?.isEmpty()
                                            ?.let { if (!it) errorResponse.message else "Something went wrong" })
                                }
                            } ?: {
                                _addAddressState.value = AppState.Error("Something went wrong")
                            }
                        } else {
                            _addAddressState.value = AppState.Error("Something went wrong")
                        }
                    }

                } catch (ex: Exception) {
                    _addAddressState.value = AppState.Error(ex.message.toString())
                }
            }
        } else return
    }

    private fun resetState(){
        _deleteAddressState.value = AppState.Idle
        _togglePrimaryAddressState.value = AppState.Idle
        _addAddressState.value = AppState.Idle
    }


}