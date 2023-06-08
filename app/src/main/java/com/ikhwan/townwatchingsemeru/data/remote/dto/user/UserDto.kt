package com.ikhwan.townwatchingsemeru.data.remote.dto.user


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.User

data class UserDto(
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
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("password")
    val password: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

fun UserDto.toUser(): User {
    return User(
        id = id,
        image = image,
        name = name,
        email = email,
        verified = verified
    )
}