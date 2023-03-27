package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdateAvaResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdatePasswordResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
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
import retrofit2.http.*

interface UserApi {
    @GET("user/category?")
    suspend fun getCategoryUser(
        @Query("id") id: Int? = null
    ): List<CategoryUserDto>

    @POST("user/login")
    suspend fun loginUser(
        @Body user : LoginBody
    ) : PostLoginResponseDto

    @POST("user/register")
    suspend fun registerUser(
        @Body registerBody: RegisterBody
    ): RegisterResponseDto

    @POST("user/verify-code")
    suspend fun verifyCode(
        @Body verifyCodeBody: VerifyCodeBody
    ): RegisterResponseDto

    @POST("user/resend-code")
    suspend fun resendRegisterCode(
        @Body resendCodeBody: ResendCodeBody
    ): RegisterResponseDto

    @PUT("user/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordBody: ResetPasswordBody,
    ): UpdatePasswordResponseDto

    @GET("user")
    suspend fun getUser(
        @Header("Authorization") auth : String
    ) : UserDto

    @PUT("user")
    suspend fun updateProfile(
        @Header("Authorization") auth : String,
        @Body updateProfileBody: UpdateProfileBody
    ): UpdateProfileResponseDto

    @PUT("user/password")
    suspend fun editPassword(
        @Header("Authorization") auth : String,
        @Body editPassword: EditPasswordBody,
    ): UpdatePasswordResponseDto

    @Multipart
    @PUT("user/profile")
    suspend fun updateAva(
        @Header("Authorization") auth : String,
        @Part image : MultipartBody.Part
    ): UpdateAvaResponseDto
}