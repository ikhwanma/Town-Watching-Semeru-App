package com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.AddCommentResponse

data class AddCommentResponseDto(
    @SerializedName("message")
    val message: String
)

fun AddCommentResponseDto.toAddCommentResponse(): AddCommentResponse{
    return AddCommentResponse(message = message)
}
