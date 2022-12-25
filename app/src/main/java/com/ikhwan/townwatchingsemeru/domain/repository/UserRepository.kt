package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto

interface UserRepository {
    suspend fun getCategoryUser(
        id: Int? =null,
    ): List<CategoryUserDto>

    suspend fun loginUser(
        user : LoginBody
    ) : PostLoginResponseDto
}