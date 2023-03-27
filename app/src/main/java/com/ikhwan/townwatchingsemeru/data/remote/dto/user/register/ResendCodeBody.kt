package com.ikhwan.townwatchingsemeru.data.remote.dto.user.register

import com.google.gson.annotations.SerializedName

data class ResendCodeBody(
    @SerializedName("email")
    val email: String
)
