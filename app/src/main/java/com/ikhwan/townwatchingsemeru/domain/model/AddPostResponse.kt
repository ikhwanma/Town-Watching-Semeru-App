package com.ikhwan.townwatchingsemeru.domain.model

data class AddPostResponse(
    val categoryId: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val latitude: String,
    val level: String,
    val detailCategory: String,
    val longitude: String,
    val status: String,
    val updatedAt: String,
    val userId: Int
)
