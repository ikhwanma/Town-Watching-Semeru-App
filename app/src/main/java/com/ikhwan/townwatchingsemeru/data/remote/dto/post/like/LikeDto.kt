package com.ikhwan.townwatchingsemeru.data.remote.dto.post.like


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.domain.model.Like

data class LikeDto(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("post")
    val post: PostDto,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
)

fun LikeDto.toLike(): Like {
    return Like(id = id, post = post)
}