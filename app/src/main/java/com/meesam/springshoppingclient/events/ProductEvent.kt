package com.meesam.springshoppingclient.events

sealed class ProductEvent {
    data class LoadProductById(val id: String): ProductEvent()
    data object ProductCountIncrement: ProductEvent()
    data object ProductCountDecrement: ProductEvent()
}