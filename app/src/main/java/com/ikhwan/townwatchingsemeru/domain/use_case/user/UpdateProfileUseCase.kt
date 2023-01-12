package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.toUpdateProfileResponse
import com.ikhwan.townwatchingsemeru.domain.model.UpdateProfileResponse
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(
        auth: String,
        updateProfileBody: UpdateProfileBody
    ): Flow<Resource<UpdateProfileResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    repository.updateProfile(auth, updateProfileBody).toUpdateProfileResponse()
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }
}