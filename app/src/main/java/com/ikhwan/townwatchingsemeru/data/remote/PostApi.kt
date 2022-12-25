package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeResponseDto
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse
import retrofit2.Response
import retrofit2.http.*

interface PostApi {
    @GET("post")
    suspend fun getAllPost(
        @Query("categoryId") categoryId: Int? = null,
        @Query("level") level: String? = null,
        @Query("status") status: Int? = null,
    ) : List<PostDto>

    @POST("post/like/{id}")
    suspend fun addLike(
        @Header("Authorization") auth : String,
        @Path("id")postId : Int
    ) : LikeResponseDto

    @GET("post/category")
    suspend fun getAllCategory() : List<CategoryDto>
}