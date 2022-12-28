package com.ikhwan.townwatchingsemeru.domain.model

data class RegisterResponse(
    val categoryUserId: String,
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val password: String
)
