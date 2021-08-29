package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.CategoryResponse
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: Repository): ViewModel() {
    private val _categoryState = mutableStateOf<NetworkResponse<CategoryResponse>>(NetworkResponse.Initialized())
    val categoryState: State<NetworkResponse<CategoryResponse>> = _categoryState

    fun setCategoryState() = viewModelScope.launch {
        _categoryState.value = NetworkResponse.Loading()
        _categoryState.value = repository.getAllCategories()
    }
}