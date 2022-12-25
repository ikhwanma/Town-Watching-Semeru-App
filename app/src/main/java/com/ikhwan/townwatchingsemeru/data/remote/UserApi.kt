package com.ikhwan.townwatchingsemeru.data.remote

import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.CategoryUserDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.PostLoginResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("user/category?")
    suspend fun getCategoryUser(
        @Query("id") id: Int? = null
    ): List<CategoryUserDto>

    @POST("user/login")
    suspend fun loginUser(
        @Body user : LoginBody
    ) : PostLoginResponseDto
}