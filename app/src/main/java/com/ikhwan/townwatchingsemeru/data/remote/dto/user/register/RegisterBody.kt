package com.ikhwan.townwatchingsemeru.data.remote.dto.user.register

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("categoryUserId")
    val categoryUserId: Int
)
