package com.ikhwan.townwatchingsemeru.data.remote.dto.post

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.DeletePostResponse

data class DeletePostResponseDto(
    @SerializedName("message")
    val message: String
)

fun DeletePostResponseDto.toDeletePostResponse(): DeletePostResponse{
    return DeletePostResponse(message)
}
