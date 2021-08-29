package com.example.mealrecipees.repository

import android.util.Log
import com.example.mealrecipees.dataModels.Category
import com.example.mealrecipees.dataModels.CategoryResponse
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.utils.Url
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.tasks.await

class Repository(
    private val client: HttpClient,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    private suspend inline fun <reified T> baseGet(
        url: String?,
        params: HashMap<String, Any>? = null
    ): NetworkResponse<T> {
        return try {
            val response = `access$client`.get<T>("${Url.BASE_URL}$url") {
                if (params != null)
                    for ((key, value) in params) {
                        parameter(key, value)
                    }
            }
            NetworkResponse.Success(response)
        } catch (exception: Exception) {
            Log.d("Repository", "baseGet: ${exception.message}")
            NetworkResponse.Error(exception.message)
        }
    }

    suspend fun getMealByName(name: String): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "s" to name
            )
        )
        return baseGet(Url.MEAL_BY_LETTER_OR_NAME, params)
    }

    suspend fun getMealByLetter(letter: Char): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "f" to letter
            )
        )
        return baseGet(Url.MEAL_BY_LETTER_OR_NAME, params)
    }

    suspend fun getMealById(id: Int): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "i" to id
            )
        )
        return baseGet(Url.MEAL_BY_ID, params)
    }

    suspend fun getRandomMeal(): NetworkResponse<MealResponse> {
        return baseGet(Url.RANDOM)
    }

    suspend fun getAllCategories(): NetworkResponse<CategoryResponse> {
        return baseGet(Url.CATEGORIES)
    }

    suspend fun getMealsByMainIngredient(main: String): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "i" to main
            )
        )
        return baseGet(Url.MAIN_INGREDIENT_OR_CATEGORY, params)
    }

    suspend fun getMealsByMainCategory(cat: String): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "c" to cat
            )
        )
        return baseGet(Url.MAIN_INGREDIENT_OR_CATEGORY, params)
    }

    suspend fun login(username: String, password: String): String? {
        return try {
            auth.signInWithEmailAndPassword(username, password).await()
            when {
                auth.currentUser == null -> "false"
                auth.currentUser?.isEmailVerified == true -> "true"
                else -> {
                    auth.signOut()
                    "email not verified"
                }
            }
        } catch (exception: Exception) {
            Log.d("Repository", "login: ${exception.message}")
            exception.message
        }
    }

    private suspend fun register(username: String, password: String): String? {
        return try {
            auth.createUserWithEmailAndPassword(username, password).await()
            "true"
        } catch (exception: Exception) {
            Log.d("Repository", "register: ${exception.message}")
            exception.message
        }
    }

    private suspend fun authenticate() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    suspend fun createNewAccount(username: String, password: String): String? {
        return try {
            if (register(username, password) == "true") {
                authenticate()
                auth.signOut()
                "true"
            } else
                "false"
        } catch (exception: Exception) {
            Log.d("Repository", "createNewAccount: ${exception.message}")
            exception.message
        }
    }

    @PublishedApi
    internal val `access$client`: HttpClient
        get() = client
}