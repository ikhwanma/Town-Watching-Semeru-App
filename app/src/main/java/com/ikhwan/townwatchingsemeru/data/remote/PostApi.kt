package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.AddPostResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.DeletePostResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostApi {
    @Multipart
    @POST("post")
    suspend fun addPost(
        @Header("Authorization") auth : String,
        @Part("description") description : RequestBody,
        @Part("latitude") latitude : RequestBody,
        @Part("longitude") longitude : RequestBody,
        @Part("categoryId") category : RequestBody,
        @Part("level") level : RequestBody,
        @Part("status") status : RequestBody,
        @Part image : MultipartBody.Part?
    ): AddPostResponseDto

    @GET("post")
    suspend fun getAllPost(
        @Query("categoryId") categoryId: Int? = null,
        @Query("level") level: String? = null,
        @Query("status") status: Int? = null,
    ) : List<PostDto>

    @GET("post/{id}")
    suspend fun getDetailPost(
        @Path("id")id : Int
    ): PostDto

    @GET("post/user")
    suspend fun getUserPost(
        @Header("Authorization") auth : String,
    ) : List<PostDto>

    @DELETE("post/user/{id}")
    suspend fun deletePostUser(
        @Header("Authorization") auth : String,
        @Path("id")id : Int
    ): DeletePostResponseDto


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

    @GET("post/like")
    suspend fun getUserLike(
        @Header("Authorization") auth : String
    ): List<LikeDto>
}