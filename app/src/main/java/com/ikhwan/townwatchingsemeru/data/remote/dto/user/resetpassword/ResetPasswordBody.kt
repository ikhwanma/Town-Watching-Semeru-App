package com.ikhwan.townwatchingsemeru.data.remote.dto.user.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String
)
