package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.AddPostResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.DeletePostResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.DeleteCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost.UpdatePostBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost.UpdatePostResponseDto
import com.ikhwan.townwatchingsemeru.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path

interface PostRepository {
    suspend fun addPost(
        auth: String,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        address: RequestBody,
        category: RequestBody,
        level: RequestBody,
        status: RequestBody,
        detailCategory: RequestBody,
        image: MultipartBody.Part,
    ): AddPostResponseDto

    suspend fun getAllPost(
        categoryId: Int? =null,
        level: String? = null,
        status: Int? = null
    ): List<PostDto>

    suspend fun updatePost(
        auth: String,
        updatePostBody: UpdatePostBody
    ): UpdatePostResponseDto

    suspend fun getDetailPost(id: Int): PostDto

    suspend fun getUserPost(auth: String): List<PostDto>

    suspend fun deletePostUser(auth: String, id: Int): DeletePostResponseDto

    suspend fun addLike(auth : String, postId : Int): LikeResponseDto

    suspend fun getAllCategory(): List<CategoryDto>

    suspend fun getComment(postId : Int): List<CommentDto>

    suspend fun addComment(auth : String, comment: CommentBody): AddCommentResponseDto

    suspend fun deleteComment(auth: String, id: Int): DeleteCommentResponseDto

    suspend fun getUserLike(auth: String): List<LikeDto>

    suspend fun getPostLike(id: Int): List<GetLikeResponse>
}