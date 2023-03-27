package com.ikhwan.townwatchingsemeru.data.remote.dto.user.register

import com.google.gson.annotations.SerializedName

data class VerifyCodeBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: Int
)
