package com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile

import com.google.gson.annotations.SerializedName

data class UpdateProfileBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("categoryUserId")
    val categoryUserId: Int
)
