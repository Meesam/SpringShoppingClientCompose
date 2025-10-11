package com.meesam.springshoppingclient.states

sealed class AppState<out T>{
    data class Success<out T>(val data: T) : AppState<T>()
    data class Error(val errorMessage: String? = "") : AppState<Nothing>()
    data object Loading :AppState<Nothing>()
    object Idle : AppState<Nothing>()
}
