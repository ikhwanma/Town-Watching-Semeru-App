package com.ikhwan.townwatchingsemeru.data.remote.dto.post.like


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse

data class LikeResponseDto(
    @SerializedName("message")
    val message: String
)

fun LikeResponseDto.toLikeResponse(): LikeResponse{
    return LikeResponse(message = message)
}