package com.ikhwan.townwatchingsemeru.data.remote.dto.post


import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.AddPostResponse

data class AddPostResponseDto(
    @SerializedName("categoryId")
    val categoryId: String,
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
    @SerializedName("detailCategory")
    val detailCategory: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int
)

fun AddPostResponseDto.toAddPostResponse(): AddPostResponse{
    return AddPostResponse(
        categoryId = categoryId,
        createdAt = createdAt,
        description = description,
        id = id,
        image = image,
        latitude = latitude,
        level = level,
        detailCategory = detailCategory,
        longitude = longitude,
        status = status,
        updatedAt = updatedAt,
        userId = userId
    )
}