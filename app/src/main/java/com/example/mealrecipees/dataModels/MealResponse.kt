package com.example.mealrecipees.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    @SerialName("meals")
    var meals: List<Meal>? = null
)