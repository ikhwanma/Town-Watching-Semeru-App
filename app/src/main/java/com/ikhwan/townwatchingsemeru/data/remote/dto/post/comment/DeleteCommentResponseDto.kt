package com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.DeleteCommentResponse

data class DeleteCommentResponseDto(
    @SerializedName("message")
    val message: String
)

fun DeleteCommentResponseDto.toDeleteCommentResponse(): DeleteCommentResponse {
    return DeleteCommentResponse(message)
}


