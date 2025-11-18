package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.meesam.springshoppingclient.events.ProductEvent
import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.model.UserResponse
import com.meesam.springshoppingclient.repository.product.ProductRepository
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.compareTo
import kotlin.dec
import kotlin.inc


@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {

    private var _productDetail = MutableStateFlow<AppState<ProductResponse>>(AppState.Idle)
    val productDetail: StateFlow<AppState<ProductResponse>> = _productDetail.asStateFlow()

    private var _colorList = MutableStateFlow(emptyList<String>())
    val colorList: StateFlow<List<String>> = _colorList.asStateFlow()

    private var _productCounter = MutableStateFlow<Int>(1)
    val productCounter: StateFlow<Int> = _productCounter.asStateFlow()

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.LoadProductById -> {
                getProductById(event.id)
            }
            is ProductEvent.ProductCountDecrement -> {
                if (_productCounter.value > 1) {
                    _productCounter.value--
                }
            }
            is ProductEvent.ProductCountIncrement -> {
                _productCounter.value++
            }
        }
    }

    private fun getProductById(productId: Long) {
        _productDetail.value = AppState.Loading
        viewModelScope.launch {
            try {
                productRepository.getProductById(productId)
                    .collect { product ->
                        _productDetail.value = AppState.Success(product)
                    }
            } catch (ex: Exception) {
                _productDetail.value = AppState.Error(ex.message)
            }
        }
    }
}