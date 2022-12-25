package com.ikhwan.townwatchingsemeru.data.remote.dto.category

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.Category

data class CategoryDto(
    @SerializedName("category")
    val category: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)

fun CategoryDto.toCategory(): Category{
    return Category(
        category = category, id = id
    )
}