package com.ikhwan.townwatchingsemeru.domain.model

data class User(
    val id: Int,
    val image: String,
    val name: String,
    val email: String,
    val verified: Boolean
)
