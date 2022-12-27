package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdateAvaResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UpdatePasswordResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.UserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto
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

    @GET("user")
    suspend fun getUser(
        @Header("Authorization") auth : String
    ) : UserDto

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