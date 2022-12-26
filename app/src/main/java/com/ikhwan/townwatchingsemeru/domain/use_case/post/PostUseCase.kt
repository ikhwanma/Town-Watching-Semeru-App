package com.ikhwan.townwatchingsemeru.domain.use_case.post

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.category.toCategory
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.toAddCommentResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.toComment
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.toLike
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.toLikeResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.toAddPostResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.toPost
import com.ikhwan.townwatchingsemeru.domain.model.*
import com.ikhwan.townwatchingsemeru.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.http.Path
import java.io.IOException
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    fun addPost(
        auth: String,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        category: RequestBody,
        level: RequestBody,
        status: RequestBody,
        image: MultipartBody.Part,
    ): Flow<Resource<AddPostResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    repository.addPost(
                        auth,
                        description,
                        latitude,
                        longitude,
                        category,
                        level,
                        status,
                        image
                    ).toAddPostResponse()
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getAllPosts(
        categoryId: Int?,
        level: String?,
        status: Int?
    ): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    repository.getAllPost(categoryId, level, status).map { it.toPost() })
            )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getUserPost(auth: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getUserPost(auth).map { it.toPost() }))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun addLike(auth: String, postId: Int): Flow<Resource<LikeResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.addLike(auth, postId).toLikeResponse()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getAllCategory(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getAllCategory().map { it.toCategory() }))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getAllComment(postId: Int): Flow<Resource<List<Comment>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getComment(postId).map { it.toComment() }))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun addComment(auth: String, comment: CommentBody): Flow<Resource<AddCommentResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.addComment(auth, comment).toAddCommentResponse()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getUserLike(auth: String): Flow<Resource<List<Like>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getUserLike(auth).map { it.toLike() }))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }
}