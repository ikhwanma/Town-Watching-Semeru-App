package com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.domain.model.Comment

data class CommentDto(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user")
    val user: UserDto
)

fun CommentDto.toComment(): Comment {
    return Comment(
        comment = comment,
        createdAt = createdAt,
        id = id,
        postId = postId,
        user = user
    )
}