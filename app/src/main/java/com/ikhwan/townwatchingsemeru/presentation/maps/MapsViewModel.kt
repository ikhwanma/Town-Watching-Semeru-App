package com.ikhwan.townwatchingsemeru.presentation.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
): ViewModel() {
    fun getAllPosts(
        categoryId: Int? = null,
        level: String? = null,
        status: Int? = null
    ): LiveData<Resource<List<Post>>> =
        postUseCase.getAllPosts(categoryId, level, status).asLiveData()

    fun getCategory(): LiveData<Resource<List<Category>>> =
        postUseCase.getAllCategory().asLiveData()
}