package com.ikhwan.townwatchingsemeru.data.remote.dto.user.login

import com.google.gson.annotations.SerializedName

data class PostLoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String
)