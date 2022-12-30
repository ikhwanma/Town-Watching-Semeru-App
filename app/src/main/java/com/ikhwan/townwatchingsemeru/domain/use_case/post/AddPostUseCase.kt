package com.ikhwan.townwatchingsemeru.domain.use_case.post

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.toAddPostResponse
import com.ikhwan.townwatchingsemeru.domain.model.AddPostResponse
import com.ikhwan.townwatchingsemeru.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
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
}