package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.toPostLoginResponse
import com.ikhwan.townwatchingsemeru.domain.model.PostLoginResponse
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: LoginBody): Flow<Resource<PostLoginResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.loginUser(user).toPostLoginResponse()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }
}