package com.example.mealrecipees.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.CategoryResponse
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class MainScreenViewModel(private val repository: Repository) : ViewModel() {
    private val _categoryState =
        mutableStateOf<NetworkResponse<CategoryResponse>>(NetworkResponse.Initialized())
    val categoryState: State<NetworkResponse<CategoryResponse>> = _categoryState
    val meals = mutableListOf<Meal>()
    private val _mealsState =
        mutableStateOf<NetworkResponse<MealResponse>>(NetworkResponse.Initialized())
    val mealsState: State<NetworkResponse<MealResponse>> = _mealsState
    private var currentLetter = 'a'

    init {
        setCategoryState()
        setMealsState()
    }


    fun setCategoryState() = viewModelScope.launch {
        _categoryState.value = NetworkResponse.Loading()
        _categoryState.value = repository.getAllCategories()
        Log.d(
            "MainScreenViewModel",
            "setMealsState: ${_categoryState.value.data?.categories?.size}"
        )
    }


    fun setMealsState() = viewModelScope.launch {
        if(currentLetter == 'z')
            return@launch
        _mealsState.value = NetworkResponse.Loading()
        _mealsState.value = repository.getMealByLetter(currentLetter)
        meals.addAll(_mealsState.value.data?.meals ?: emptyList())
        currentLetter++
        Log.d("MainScreenViewModel", "setMealsState: ${_mealsState.value.data?.meals?.size}")
    }
}