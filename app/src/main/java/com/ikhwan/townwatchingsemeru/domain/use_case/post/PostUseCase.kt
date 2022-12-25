package com.ikhwan.townwatchingsemeru.domain.use_case.post

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.category.toCategory
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.like.toLikeResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.toPost
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.http.Path
import java.io.IOException
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    fun getAllPosts(
        categoryId: Int?,
        level: String?,
        status: Int?
    ) : Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getAllPost(categoryId, level, status).map { it.toPost() }))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun addLike(auth : String, postId : Int): Flow<Resource<LikeResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.addLike(auth, postId).toLikeResponse()))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getAllCategory(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success( repository.getAllCategory().map { it.toCategory() }))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }
}