package com.ikhwan.townwatchingsemeru.data.remote.dto.user


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto

data class User(
    @SerializedName("category_user")
    val categoryUser: CategoryUserDto,
    @SerializedName("categoryUserId")
    val categoryUserId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)