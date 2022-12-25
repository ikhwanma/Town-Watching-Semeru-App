package com.ikhwan.townwatchingsemeru.domain.model

import com.google.gson.annotations.SerializedName

data class AddPostResponse(
    val categoryId: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val latitude: String,
    val level: String,
    val longitude: String,
    val status: String,
    val updatedAt: String,
    val userId: Int
)
