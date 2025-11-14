package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.ProductEvent
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.model.ProductResponse
import com.meesam.springshoppingclient.repository.product.ProductRepository
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private var _productsState = MutableStateFlow<AppState<List<ProductResponse>>>(AppState.Loading)
    val productsState: StateFlow<AppState<List<ProductResponse>>> = _productsState.asStateFlow()

    private var _productsHistory =
        MutableStateFlow<AppState<List<ProductResponse>>>(AppState.Loading)
    val productsHistory: StateFlow<AppState<List<ProductResponse>>> = _productsHistory.asStateFlow()

    private var _recommendedProducts =
        MutableStateFlow<AppState<List<ProductResponse>>>(AppState.Loading)
    val recommendedProducts: StateFlow<AppState<List<ProductResponse>>> =
        _recommendedProducts.asStateFlow()

    private var _productDetail = MutableStateFlow<AppState<ProductResponse>>(AppState.Idle)
    val productDetail: StateFlow<AppState<ProductResponse>> = _productDetail.asStateFlow()

    private var productCounter = MutableStateFlow<Int>(1)
    val _productCounter: StateFlow<Int> = productCounter.asStateFlow()

    init {
        getAllProduct()
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.LoadProductById -> {
                getProductById(event.id)
            }

            is ProductEvent.ProductCountDecrement -> {
                if (productCounter.value > 1) {
                    productCounter.value--
                }
            }

            is ProductEvent.ProductCountIncrement -> {
                productCounter.value++
            }
        }
    }

    private fun getAllProduct() {
        _productsState.value = AppState.Loading
        viewModelScope.launch {
            try {
                val result = productRepository.getAllProduct()
                _productsState.value = AppState.Success(result.body()!!)
                _productsHistory.value = AppState.Success(result.body()!!)
                _recommendedProducts.value = AppState.Success(result.body()!!)
            } catch (ex: Exception) {
                _productsState.value = AppState.Error(ex.message.toString())
            }
        }
    }

    private fun getProductById(productId: Long) {
        _productDetail.value = AppState.Loading
        viewModelScope.launch {
            try {
                val result = productRepository.getProductById(productId)
                _productDetail.value = AppState.Success(result.body()!!)

            } catch (ex: Exception) {
                _productDetail.value = AppState.Error(ex.message)
            }
        }
    }

}