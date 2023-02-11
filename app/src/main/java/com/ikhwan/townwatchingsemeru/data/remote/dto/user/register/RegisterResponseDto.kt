package com.ikhwan.townwatchingsemeru.data.remote.dto.user.register


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse

data class RegisterResponseDto(
    @SerializedName("message")
    val message: String,
)

fun RegisterResponseDto.toRegisterResponse(): RegisterResponse {
    return RegisterResponse(
        message = message
    )
}

