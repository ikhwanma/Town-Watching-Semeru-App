package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.toRegisterResponse
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ResendCodeUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(resendCodeBody: ResendCodeBody): Flow<Resource<RegisterResponse>> =
        flow {
            try {
                emit(Resource.Loading())
                emit(
                    Resource.Success(
                        repository.resendRegisterCode(resendCodeBody).toRegisterResponse()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server, check connection"))
            }
        }
}