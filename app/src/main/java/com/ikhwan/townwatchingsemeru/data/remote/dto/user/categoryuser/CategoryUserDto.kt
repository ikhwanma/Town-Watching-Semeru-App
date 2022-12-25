package com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser

data class CategoryUserDto(
    @SerializedName("category_user")
    val categoryUser: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updatedAt")
    val updatedAt: String
)

fun CategoryUserDto.toCategoryUser(): CategoryUser {
    return CategoryUser(categoryUser = categoryUser, id = id)
}