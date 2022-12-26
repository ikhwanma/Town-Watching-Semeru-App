package com.ikhwan.townwatchingsemeru.domain.model

import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto

data class Like(
    val id: Int,
    val post: PostDto
)
