package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.ProductEvent
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel @Inject constructor()  :
    ViewModel() {

    private var products = MutableStateFlow<AppState<List<Product?>>>(AppState.Loading)
    val _products: StateFlow<AppState<List<Product?>>> = products.asStateFlow()

    private var _productDetail = MutableStateFlow<AppState<Product?>>(AppState.Idle)
    val productDetail: StateFlow<AppState<Product?>> = _productDetail.asStateFlow()
    private var productId = ""

    private var productCounter = MutableStateFlow<Int>(1)
    val _productCounter: StateFlow<Int> = productCounter.asStateFlow()

    init {
        //getAllProduct()
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.LoadProductById -> {
                productId = event.id
               // getProductById()
            }

            is ProductEvent.ProductCountDecrement -> {
                if(productCounter.value > 1){
                    productCounter.value --
                }
            }
            is ProductEvent.ProductCountIncrement -> {
                productCounter.value ++
            }
        }
    }

   /* private fun getAllProduct() {
        products.value = AppState.Loading
        viewModelScope.launch {
            try {
                val result = productRepository.getAllProducts()
                products.value = AppState.Success(result)
            } catch (ex: Exception) {
                products.value = AppState.Error(ex.message.toString())
            }
        }
    }*/

    /*private fun getProductById() {
        _productDetail.value = AppState.Loading
        viewModelScope.launch {
            try {
                val result = productRepository.getProductById(productId)
                _productDetail.value = AppState.Success(result)

            } catch (ex: Exception) {
                _productDetail.value = AppState.Error(ex.message)
            }
        }
    }*/

}