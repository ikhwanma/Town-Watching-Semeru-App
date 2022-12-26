package com.ikhwan.townwatchingsemeru.data.repository

import com.ikhwan.townwatchingsemeru.data.remote.UserApi
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdatePasswordResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository{

    override suspend fun getCategoryUser(id: Int?): List<CategoryUserDto> {
        return api.getCategoryUser(id)
    }

    override suspend fun loginUser(user: LoginBody): PostLoginResponseDto {
        return api.loginUser(user)
    }

    override suspend fun getUser(auth: String): UserDto {
        return api.getUser(auth)
    }

    override suspend fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): UpdatePasswordResponseDto {
        return api.editPassword(auth, editPassword)
    }

}