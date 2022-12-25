package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto


interface UserRepository {
    suspend fun getCategoryUser(
        id: Int? =null,
    ): List<CategoryUserDto>
}