package com.ikhwan.townwatchingsemeru.domain.model

import com.google.gson.annotations.SerializedName

data class PostLoginResponse(
    val message: String,
    val token: String,
    val id: Int
)
