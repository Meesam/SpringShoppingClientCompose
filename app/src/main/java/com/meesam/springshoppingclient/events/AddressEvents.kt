package com.meesam.springshoppingclient.events

sealed class AddressEvents {
    data class OnSelectAddressType(val addressType: String):AddressEvents()
    data object OnSaveChangesClick :AddressEvents()
    data class OnAddressToggleClick(val addressId: Long) : AddressEvents()
    data class OnDeleteAddressClick(val addressId: Long) : AddressEvents()
    data object LoadUserAddressList: AddressEvents()

    data object OnResetState:AddressEvents()
}