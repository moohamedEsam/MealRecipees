package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mealrecipees.dataModels.Meal

class MealViewModel : ViewModel(){
    var meal : Meal? = null
    private val _choice = mutableStateOf<String>("")
    val choice: State<String> = _choice

    fun setChoice(value: String){
    	_choice.value = value
    }
}