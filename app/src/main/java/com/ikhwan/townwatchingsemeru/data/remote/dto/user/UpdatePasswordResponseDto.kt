package com.ikhwan.townwatchingsemeru.data.remote.dto.user


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePasswordResponse

data class UpdatePasswordResponseDto(
    @SerializedName("message")
    val message: String
)

fun UpdatePasswordResponseDto.toUpdatePasswordResponse(): UpdatePasswordResponse {
    return UpdatePasswordResponse(message = message)
}
