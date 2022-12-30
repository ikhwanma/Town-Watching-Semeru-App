package com.ikhwan.townwatchingsemeru.presentation.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllCategoryUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase
): ViewModel() {
    suspend fun getAllPosts(
        categoryId: Int? = null,
        level: String? = null,
        status: Int? = null
    ): LiveData<Resource<List<Post>>> =
        getAllPostsUseCase.invoke(categoryId, level, status).asLiveData()

    suspend fun getCategory(): LiveData<Resource<List<Category>>> =
        getAllCategoryUseCase.invoke().asLiveData()
}