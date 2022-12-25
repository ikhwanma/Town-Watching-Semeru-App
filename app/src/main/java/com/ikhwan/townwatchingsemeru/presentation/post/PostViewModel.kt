package com.ikhwan.townwatchingsemeru.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.AddPostResponse
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val pref: DataStoreManager
) : ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getCategory(): LiveData<Resource<List<Category>>> =
        postUseCase.getAllCategory().asLiveData()

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
        postUseCase.addPost(auth, description, latitude, longitude, category, level, status, image)
            .asLiveData()
}