package com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword

import com.google.gson.annotations.SerializedName

data class EditPasswordBody(
    @SerializedName("password")
    val password: String,
    @SerializedName("newPassword")
    val newPassword: String
)