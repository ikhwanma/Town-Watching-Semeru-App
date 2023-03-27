package com.ikhwan.townwatchingsemeru.di

import android.content.Context
import com.ikhwan.townwatchingsemeru.common.Constants
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.data.remote.PostApi
import com.ikhwan.townwatchingsemeru.data.remote.UserApi
import com.ikhwan.townwatchingsemeru.data.repository.PostRepositoryImpl
import com.ikhwan.townwatchingsemeru.data.repository.UserRepositoryImpl
import com.ikhwan.townwatchingsemeru.domain.repository.PostRepository
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val okHttpClient= OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun providePostApi(): PostApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostRepository(api: PostApi): PostRepository{
        return PostRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providesPref(@ApplicationContext appContext: Context) : DataStoreManager =  DataStoreManager(appContext)

}