package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
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

    @GET("post/comment/{id}")
    suspend fun getComment(
        @Path("id")postId : Int
    ): List<CommentDto>

    @POST("post/comment")
    suspend fun addComment(
        @Header("Authorization") auth : String,
        @Body comment: CommentBody
    ): AddCommentResponseDto
}