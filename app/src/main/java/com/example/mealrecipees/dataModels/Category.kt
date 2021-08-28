package com.example.mealrecipees.dataModels


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("idCategory")
    var idCategory: String? = null,
    @SerialName("strCategory")
    var strCategory: String? = null,
    @SerialName("strCategoryDescription")
    var strCategoryDescription: String? = null,
    @SerialName("strCategoryThumb")
    var strCategoryThumb: String? = null
)