package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.model.CategoryResponse
import com.meesam.springshoppingclient.model.Product
import com.meesam.springshoppingclient.repository.ProductRepository
import com.meesam.springshoppingclient.repository.category.CategoryRepository
import com.meesam.springshoppingclient.states.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class FeedViewModel :
    ViewModel() {
    private var _categories = MutableStateFlow<AppState<List<CategoryResponse>>>(AppState.Loading)
    val categories: StateFlow<AppState<List<CategoryResponse>>> = _categories.asStateFlow()

    private var _products = MutableStateFlow<AppState<List<Product>>>(AppState.Loading)
    val products: StateFlow<AppState<List<Product>>> = _products.asStateFlow()

    private var _productsHistory = MutableStateFlow<AppState<List<Product>>>(AppState.Loading)
    val productsHistory: StateFlow<AppState<List<Product>>> = _productsHistory.asStateFlow()

    private var _recommendedProducts = MutableStateFlow<AppState<List<Product>>>(AppState.Loading)
    val recommendedProducts: StateFlow<AppState<List<Product>>> = _recommendedProducts.asStateFlow()

    private var isLoading = MutableStateFlow<Boolean> (false)
    val _isLoading : StateFlow<Boolean> = isLoading.asStateFlow()

    init {
        //getAllCategory()
       // GetFeedScreenData()
    }

    fun onEvent(events: FeedEvents) {
        when (events) {
            is FeedEvents.onCategorySelect -> {
                viewModelScope.launch {
                    val currentState = _categories.value
                    if (currentState is AppState.Success) {
                        val currentCategory = currentState.data
                        val updatedCategory = currentCategory.map { category ->
                            if (events.categoryName == category.title) {
                                category.isSelected.let { category.copy(isSelected = !it) }
                            }else {
                                category
                            }
                        }
                        _categories.value = AppState.Success(updatedCategory) as AppState<List<CategoryResponse>>
                    }
                }
            }
        }
    }

    /*private fun getAllCategory() {
        _categories.value = AppState.Loading
        viewModelScope.launch {
            try {
                val allCategory = categoryRepository.getAllCategories()
                _categories.value = AppState.Success(allCategory)
            } catch (ex: Exception) {
                _categories.value = AppState.Error(ex.toString())
            }
        }
    }*/

    /*private fun GetFeedScreenData(){
        isLoading.value = true
        viewModelScope.launch {
            try {
                val categoriesDeferred = async { categoryRepository.getAllCategories() }
                val productsDeferred = async { productRepository.getAllProducts() }
                val allCategory = categoriesDeferred.await()
                val allProduct = productsDeferred.await()
                _categories.value = AppState.Success(allCategory)
                _products.value = AppState.Success(allProduct)
                _productsHistory.value = AppState.Success(allProduct)
                _recommendedProducts.value = AppState.Success(allProduct)
                isLoading.value = false

            }catch (ex: Exception){
                isLoading.value = false
            }
        }
    }*/

}