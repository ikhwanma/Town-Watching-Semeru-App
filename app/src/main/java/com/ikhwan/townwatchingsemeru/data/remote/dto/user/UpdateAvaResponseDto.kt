package com.ikhwan.townwatchingsemeru.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.UpdateAvaResponse

data class UpdateAvaResponseDto(
    @SerializedName("message")
    val message: String
)

fun UpdateAvaResponseDto.toUpdateAvaResponse(): UpdateAvaResponse{
    return UpdateAvaResponse(message)
}

