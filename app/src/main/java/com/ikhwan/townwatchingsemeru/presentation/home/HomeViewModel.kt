package com.ikhwan.townwatchingsemeru.presentation.home

import androidx.lifecycle.*
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.*
import com.ikhwan.townwatchingsemeru.domain.use_case.post.AddLikeUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllCategoryUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllPostsUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetPostLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getLikePostsUseCase: GetPostLikeUseCase,
    private val pref: DataStoreManager
) : ViewModel() {

    fun getAllPosts(
        categoryId: Int?,
        level: String?,
        status: Int?
    ): LiveData<Resource<List<Post>>> =
        getAllPostsUseCase.invoke(categoryId, level, status).asLiveData()

    fun addLike(auth: String, postId: Int): LiveData<Resource<LikeResponse>> =
        addLikeUseCase.invoke(auth, postId).asLiveData()

    fun getCategory(): LiveData<Resource<List<Category>>> =
        getAllCategoryUseCase.invoke().asLiveData()

    fun getPostLike(id: Int): LiveData<Resource<List<GetLikeResponse>>> =
        getLikePostsUseCase.invoke(id).asLiveData()


    fun getId(): LiveData<Int> {
        return pref.getId().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

}
