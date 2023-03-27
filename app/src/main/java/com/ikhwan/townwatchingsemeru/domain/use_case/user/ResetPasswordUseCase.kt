package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.resetpassword.ResetPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.toUpdatePasswordResponse
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePasswordResponse
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(resetPasswordBody: ResetPasswordBody): Flow<Resource<UpdatePasswordResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                emit(
                    Resource.Success(
                        repository.resetPassword(resetPasswordBody).toUpdatePasswordResponse()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server, check connection"))
            }
        }
}