package com.example.mealrecipees.utils

sealed class NetworkResponse<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : NetworkResponse<T>(data)
    class Error<T>(message: String?) : NetworkResponse<T>(error = message)
    class Loading<T> : NetworkResponse<T>()
    class Initialized<T> : NetworkResponse<T>()
}
