package com.ikhwan.townwatchingsemeru.domain.model

import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.Like
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.User

data class Post(
    val category: CategoryDto,
    val comment: List<CommentDto>,
    val description: String,
    val id: Int,
    val image: String,
    val latitude: String,
    val level: String,
    val like: List<Like>,
    val longitude: String,
    val status: Boolean,
    val createdAt: String,
    val user: User
)
