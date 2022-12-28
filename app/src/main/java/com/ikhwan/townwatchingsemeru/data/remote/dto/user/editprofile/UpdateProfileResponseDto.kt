package com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile

import com.google.gson.annotations.SerializedName
import com.ikhwan.townwatchingsemeru.domain.model.UpdateProfileResponse

data class UpdateProfileResponseDto(
    @SerializedName("message")
    val message: String
)

fun UpdateProfileResponseDto.toUpdateProfileResponse(): UpdateProfileResponse{
    return UpdateProfileResponse(message)
}
