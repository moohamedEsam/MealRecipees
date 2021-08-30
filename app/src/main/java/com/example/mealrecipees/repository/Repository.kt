package com.example.mealrecipees.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.mealrecipees.dataModels.CategoryResponse
import com.example.mealrecipees.dataModels.Meal
import com.example.mealrecipees.dataModels.MealResponse
import com.example.mealrecipees.utils.NetworkResponse
import com.example.mealrecipees.utils.Url
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    suspend fun getMealById(id: String): NetworkResponse<MealResponse> {
        val params = HashMap<String, Any>(
            mapOf(
                "i" to id
            )
        )
        return baseGet(Url.MEAL_BY_ID, params)
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

    fun signOut(context: Context): Boolean {
        return try {
            auth.signOut()
            true
        } catch (exception: Exception) {
            Log.d("Repository", "signOut: ${exception.message}")
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun likeMeal(mealId: String) {
        fireStore.collection("likes").document(auth.currentUser?.uid ?: "")
            .update("favourite", FieldValue.arrayUnion(mealId))
            .addOnFailureListener {
                val data = HashMap<String, Any>(
                    mapOf(
                        "favourite" to listOf(mealId)
                    )
                )
                fireStore.collection("likes").document(auth.currentUser?.uid ?: "")
                    .set(data)
            }
    }

    fun unlikeMeal(mealId: String) {
        fireStore.collection("likes").document(auth.currentUser?.uid ?: "")
            .update("favourite", FieldValue.arrayRemove(mealId))
    }

    suspend fun isLiked(mealId: String): Boolean {
        fireStore.collection("likes")
            .document(auth.currentUser?.uid ?: "")
            .get().await().data.let {
                return (it?.containsKey("favourite") == true && (it["favourite"] as List<*>).contains(
                    mealId
                ))
            }
    }

    suspend fun getLikedMeals(likes: MutableState<NetworkResponse<List<Meal>>>) {
        likes.value = NetworkResponse.Loading()
        fireStore.collection("likes")
            .document(auth.currentUser?.uid ?: "")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("Repository", "getLikedMeals: ${error.message}")
                    likes.value = NetworkResponse.Error(error.message)
                    return@addSnapshotListener
                }
                value?.get("favourite")?.let {
                    val meals = mutableListOf<Meal>()
                    CoroutineScope(Dispatchers.IO).launch {
                        (it as List<*>).forEach { id ->
                            val response = getMealById(id as String)
                            if (response is NetworkResponse.Success) {
                                response.data?.meals?.get(0)?.let { meal ->
                                    meals.add(meal)
                                }
                            }
                        }
                        likes.value = NetworkResponse.Success(meals)
                    }
                }
            }
    }

    @PublishedApi
    internal val `access$client`: HttpClient
        get() = client
}