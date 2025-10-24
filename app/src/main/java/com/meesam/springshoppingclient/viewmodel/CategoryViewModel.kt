package com.meesam.springshoppingclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meesam.springshoppingclient.events.FeedEvents
import com.meesam.springshoppingclient.states.AppState
import com.meesam.springshoppingclient.views.category.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {
    private var _categories = MutableStateFlow<AppState<List<CategoryItem>>>(AppState.Loading)
    val categories: StateFlow<AppState<List<CategoryItem>>> = _categories.asStateFlow()

    init {
        //getAllCategory()
        // GetFeedScreenData()
        val categoryList = listOf<CategoryItem>(
            CategoryItem(
                id = 1,
                title = "All",
                isSelected = true
            ),
            CategoryItem(
                id = 2,
                title = "Electronics",
                isSelected = false
            ),
            CategoryItem(
                id = 3,
                title = "Books",
                isSelected = false
            ),
            CategoryItem(
                id = 4,
                title = "Clothes",
                isSelected = false
            ),
            CategoryItem(
                id = 5,
                title = "Fashion",
                isSelected = false
            ),
            CategoryItem(
                id = 6,
                title = "Home Appliances",
                isSelected = false
            ),
            CategoryItem(
                id = 7,
                title = "Mobile",
                isSelected = false
            ),
            CategoryItem(
                id = 8,
                title = "Kid's Wear",
                isSelected = false
            ),
            CategoryItem(
                id = 9,
                title = "Toys",
                isSelected = false
            ),
        )
        _categories.value= AppState.Success(categoryList) // as AppState<List<CategoryItem>>
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
                                category.copy(isSelected = false)
                            }
                        }
                        _categories.value = AppState.Success(updatedCategory)
                    }
                }
            }
        }
    }
}