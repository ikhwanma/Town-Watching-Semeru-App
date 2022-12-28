package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.toCategoryUser
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.toPostLoginResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.toRegisterResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.toUpdateAvaResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.toUpdatePasswordResponse
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.toUser
import com.ikhwan.townwatchingsemeru.domain.model.*
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    fun getCategoryUser(): Flow<Resource<List<CategoryUser>>> = flow {
        try {
            emit(Resource.Loading())
            val listCategoryUser = repository.getCategoryUser().map { it.toCategoryUser() }
            emit(Resource.Success(listCategoryUser))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun loginUser(user: LoginBody): Flow<Resource<PostLoginResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.loginUser(user).toPostLoginResponse()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun registerUser(registerBody: RegisterBody): Flow<Resource<RegisterResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.registerUser(registerBody).toRegisterResponse()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun getUser(auth: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.getUser(auth).toUser()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): Flow<Resource<UpdatePasswordResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    repository.editPassword(auth, editPassword).toUpdatePasswordResponse()
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }

    fun updateAva(auth: String, image: MultipartBody.Part): Flow<Resource<UpdateAvaResponse>> =
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