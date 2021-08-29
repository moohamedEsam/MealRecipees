package com.example.mealrecipees.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealrecipees.repository.Repository
import com.example.mealrecipees.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    private val _userState =
        MutableStateFlow<NetworkResponse<String>>(NetworkResponse.Initialized())
    val userState: StateFlow<NetworkResponse<String>> = _userState

    fun signUp(username: String, password: String, context: Context) = viewModelScope.launch {
        _userState.emit(NetworkResponse.Loading())
        repository.createNewAccount(username, password).let {
            if(it == "true") {
                _userState.emit(NetworkResponse.Success(""))
                Toast.makeText(
                    context,
                    "account created verify account to login",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
                _userState.emit(NetworkResponse.Error(it))
        }
    }


}