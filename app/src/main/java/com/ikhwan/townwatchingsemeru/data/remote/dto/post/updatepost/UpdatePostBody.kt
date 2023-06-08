package com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost

import com.google.gson.annotations.SerializedName

data class UpdatePostBody(
    @SerializedName("id")
    val id: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("level")
    val level: String,
    @SerializedName("status")
    val status: Int
)
