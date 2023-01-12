package com.ikhwan.townwatchingsemeru.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.AddPostResponse
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.use_case.post.AddPostUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllCategoryUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val addPostsUseCase: AddPostUseCase,
    private val pref: DataStoreManager
) : ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getCategory(): LiveData<Resource<List<Category>>> =
        getAllCategoryUseCase.invoke().asLiveData()

    fun addPost(
        auth: String,
        description: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        category: RequestBody,
        level: RequestBody,
        status: RequestBody,
        image: MultipartBody.Part
    ): LiveData<Resource<AddPostResponse>> =
        addPostsUseCase.invoke(
            auth = auth,
            description = description,
            latitude = latitude,
            longitude = longitude,
            category = category,
            level = level,
            status = status,
            image = image
        )
            .asLiveData()
}