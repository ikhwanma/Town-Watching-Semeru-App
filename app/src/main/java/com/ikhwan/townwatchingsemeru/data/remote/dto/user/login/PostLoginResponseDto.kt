package com.ikhwan.townwatchingsemeru.data.remote.dto.user.login

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.PostLoginResponse

data class PostLoginResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("id")
    val id: Int
)

fun PostLoginResponseDto.toPostLoginResponse(): PostLoginResponse {
    return PostLoginResponse(message = message, token = token, id = id)
}