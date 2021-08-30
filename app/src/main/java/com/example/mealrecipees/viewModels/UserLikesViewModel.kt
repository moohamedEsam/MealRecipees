package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class UserLikesViewModel(private val repository: Repository) : ViewModel() {
    private val _userLikes =
        mutableStateOf<NetworkResponse<List<Meal>>>(NetworkResponse.Initialized())
    val userLikes: State<NetworkResponse<List<Meal>>> = _userLikes


    fun setUserLikes() = viewModelScope.launch {
        repository.getLikedMeals(_userLikes)
    }
}