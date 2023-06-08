package com.ikhwan.townwatchingsemeru.domain.repository

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdateAvaResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdatePasswordResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.VerifyCodeBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.resetpassword.ResetPasswordBody
import okhttp3.MultipartBody

interface UserRepository {

    suspend fun loginUser(
        user : LoginBody
    ): PostLoginResponseDto

    suspend fun registerUser(
        registerBody: RegisterBody
    ): RegisterResponseDto

    suspend fun verifyCode(
        verifyCodeBody: VerifyCodeBody
    ): RegisterResponseDto

    suspend fun resendRegisterCode(
        resendCodeBody: ResendCodeBody
    ): RegisterResponseDto

    suspend fun resetPassword(
        resetPasswordBody: ResetPasswordBody
    ): UpdatePasswordResponseDto

    suspend fun getUser(
         auth : String
    ): UserDto

    suspend fun updateProfile(
        auth : String,
        updateProfileBody: UpdateProfileBody
    ): UpdateProfileResponseDto

    suspend fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): UpdatePasswordResponseDto

    suspend fun updateAva(
        auth : String,
        image : MultipartBody.Part
    ): UpdateAvaResponseDto
}