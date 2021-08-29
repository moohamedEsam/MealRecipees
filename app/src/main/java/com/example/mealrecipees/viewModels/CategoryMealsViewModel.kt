package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class CategoryMealsViewModel(private val repository: Repository) : ViewModel() {
    var category = ""
    private val _categoryState =
        mutableStateOf<NetworkResponse<MealResponse>>(NetworkResponse.Initialized())
    val categoryState: State<NetworkResponse<MealResponse>> = _categoryState

    fun setCategoryState() = viewModelScope.launch {
        _categoryState.value = NetworkResponse.Loading()
        _categoryState.value = repository.getMealsByMainCategory(cat = category)
    }

}