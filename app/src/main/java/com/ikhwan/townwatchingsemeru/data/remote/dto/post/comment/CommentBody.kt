package com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment

import com.google.gson.annotations.SerializedName

data class CommentBody(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("postId")
    val postId: Int
)