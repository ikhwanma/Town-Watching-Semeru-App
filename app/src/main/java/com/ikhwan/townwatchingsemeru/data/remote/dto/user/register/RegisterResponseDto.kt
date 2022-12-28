package com.ikhwan.townwatchingsemeru.data.remote.dto.user.register


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse

data class RegisterResponseDto(
    @SerializedName("categoryUserId")
    val categoryUserId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String
)

fun RegisterResponseDto.toRegisterResponse(): RegisterResponse {
    return RegisterResponse(
        categoryUserId = categoryUserId,
        email = email,
        id = id,
        image = image,
        name = name,
        password = password
    )
}

