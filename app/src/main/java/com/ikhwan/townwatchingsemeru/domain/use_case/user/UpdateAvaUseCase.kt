package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.toUpdateAvaResponse
import com.ikhwan.townwatchingsemeru.domain.model.UpdateAvaResponse
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateAvaUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        auth: String,
        image: MultipartBody.Part
    ): Flow<Resource<UpdateAvaResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                emit(Resource.Success(repository.updateAva(auth, image).toUpdateAvaResponse()))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server, check connection"))
            }
        }
}