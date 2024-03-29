package com.ikhwan.townwatchingsemeru.data.repository

import com.ikhwan.townwatchingsemeru.data.remote.UserApi
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
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository{

    override suspend fun loginUser(user: LoginBody): PostLoginResponseDto {
        return api.loginUser(user)
    }

    override suspend fun registerUser(registerBody: RegisterBody): RegisterResponseDto {
        return api.registerUser(registerBody)
    }

    override suspend fun verifyCode(verifyCodeBody: VerifyCodeBody): RegisterResponseDto {
        return api.verifyCode(verifyCodeBody)
    }

    override suspend fun resendRegisterCode(resendCodeBody: ResendCodeBody): RegisterResponseDto {
        return api.resendRegisterCode(resendCodeBody)
    }

    override suspend fun resetPassword(resetPasswordBody: ResetPasswordBody): UpdatePasswordResponseDto {
        return api.resetPassword(resetPasswordBody)
    }

    override suspend fun getUser(auth: String): UserDto {
        return api.getUser(auth)
    }

    override suspend fun updateProfile(
        auth: String,
        updateProfileBody: UpdateProfileBody
    ): UpdateProfileResponseDto {
        return api.updateProfile(auth, updateProfileBody)
    }

    override suspend fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): UpdatePasswordResponseDto {
        return api.editPassword(auth, editPassword)
    }

    override suspend fun updateAva(auth: String, image: MultipartBody.Part): UpdateAvaResponseDto {
        return api.updateAva(auth, image)
    }

}