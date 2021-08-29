package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class MealViewModel(private val repository: Repository) : ViewModel() {
    private val _meal = mutableStateOf(Meal())
    val meal: State<Meal> = _meal

    fun setMeal(value: Meal) {
        _meal.value = value
    }

    private val _choice = mutableStateOf("")
    val choice: State<String> = _choice

    fun setChoice(value: String) {
        _choice.value = value
    }

    fun checkMeal() = viewModelScope.launch {
        if (_meal.value.strIngredient1 == null || _meal.value.strIngredient1 == "") {
            repository.getMealById(meal.value.idMeal ?: "0").let {
                if (it is NetworkResponse.Success) {
                    _meal.value = it.data?.meals?.get(0) ?: meal.value
                }
            }
        }
    }
}