package com.ikhwan.townwatchingsemeru.domain.model

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto

data class Comment(
    val comment: String,
    val createdAt: String,
    val id: Int,
    val postId: Int,
    val user: UserDto
)
