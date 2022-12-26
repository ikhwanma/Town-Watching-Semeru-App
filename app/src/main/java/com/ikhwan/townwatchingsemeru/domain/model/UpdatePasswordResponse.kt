package com.ikhwan.townwatchingsemeru.domain.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(
    @SerializedName("message")
    val message: String
)
