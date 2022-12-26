package com.ikhwan.townwatchingsemeru.domain.model

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto

data class User(
    val categoryUser: CategoryUserDto,
    val id: Int,
    val image: String,
    val name: String,
)
