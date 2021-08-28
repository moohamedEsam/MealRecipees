package com.example.mealrecipees.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _userStatus =
        MutableStateFlow<NetworkResponse<String>>(NetworkResponse.Initialized())
    val userStatus: StateFlow<NetworkResponse<String>> = _userStatus

    fun signIn(username: String, password: String) = viewModelScope.launch {
        _userStatus.emit(NetworkResponse.Loading())
        repository.login(username, password).let {
            if (it == "true")
                _userStatus.emit(NetworkResponse.Success(""))
            else
                _userStatus.emit(NetworkResponse.Error(it))
        }
    }
}