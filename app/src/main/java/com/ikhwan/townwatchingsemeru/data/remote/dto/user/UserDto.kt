package com.ikhwan.townwatchingsemeru.data.remote.dto.user


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.domain.model.User

data class UserDto(
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

fun UserDto.toUser(): User {
    return User(
        categoryUser = categoryUser, id = id, image = image, name = name
    )
}