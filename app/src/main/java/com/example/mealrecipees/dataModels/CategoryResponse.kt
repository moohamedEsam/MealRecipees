package com.example.mealrecipees.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("categories")
    var categories: List<Category>? = null
)