package com.ikhwan.townwatchingsemeru.data.remote.dto.post


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.Like
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.User
import com.ikhwan.townwatchingsemeru.domain.model.Post

data class PostDto(
    @SerializedName("category")
    val category: CategoryDto,
    @SerializedName("comment")
    val comment: List<CommentDto>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("level")
    val level: String,
    @SerializedName("like")
    val like: List<Like>,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user")
    val user: User
)

fun PostDto.toPost(): Post {
    return Post(
        category = category,
        comment = comment,
        description = description,
        id = id,
        image = image,
        latitude = latitude,
        level = level,
        like = like,
        longitude = longitude,
        status = status,
        createdAt = createdAt,
        user = user
    )
}