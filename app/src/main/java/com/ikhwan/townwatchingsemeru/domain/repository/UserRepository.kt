package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdateAvaResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdatePasswordResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Part

interface UserRepository {
    suspend fun getCategoryUser(
        id: Int? =null,
    ): List<CategoryUserDto>

    suspend fun loginUser(
        user : LoginBody
    ): PostLoginResponseDto

    suspend fun getUser(
         auth : String
    ): UserDto

    suspend fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): UpdatePasswordResponseDto

    suspend fun updateAva(
        auth : String,
        image : MultipartBody.Part
    ): UpdateAvaResponseDto
}