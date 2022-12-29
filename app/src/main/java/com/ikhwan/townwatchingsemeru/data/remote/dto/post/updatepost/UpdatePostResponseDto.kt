package com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePostResponse

data class UpdatePostResponseDto(
    @SerializedName("message")
    val message: String
)

fun UpdatePostResponseDto.toUpdatePostResponse(): UpdatePostResponse {
    return UpdatePostResponse(message)
}
