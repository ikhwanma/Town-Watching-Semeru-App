package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.category.CategoryDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.PostDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.LikeResponseDto
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse
import com.ikhwan.townwatchingsemeru.domain.model.Post
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path

interface PostRepository {
    suspend fun getAllPost(
        categoryId: Int? =null,
        level: String? = null,
        status: Int? = null
    ): List<PostDto>

    suspend fun addLike(auth : String, postId : Int): LikeResponseDto

    suspend fun getAllCategory(): List<CategoryDto>

    suspend fun getComment(postId : Int): List<CommentDto>

    suspend fun addComment(auth : String, comment: CommentBody): AddCommentResponseDto
}