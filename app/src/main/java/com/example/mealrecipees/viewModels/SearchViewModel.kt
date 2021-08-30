package com.example.mealrecipees.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {
    private val _searchResult =
        mutableStateOf<NetworkResponse<MealResponse>>(NetworkResponse.Initialized())
    val searchResult: State<NetworkResponse<MealResponse>> = _searchResult

    fun setSearchResult(searchType: Int, query: String) = viewModelScope.launch {
        _searchResult.value = NetworkResponse.Loading()
        if (!(query.isNotBlank() && query.isNotEmpty())) {
            _searchResult.value = NetworkResponse.Error("query is empty")
            return@launch
        }
        when (searchType) {
            1 -> _searchResult.value = repository.getMealByLetter(query[0])
            2 -> _searchResult.value = repository.getMealsByMainCategory(query)
            3 -> _searchResult.value = repository.getMealsByMainIngredient(query)
            else -> _searchResult.value = repository.getMealByName(query)
        }
    }
}