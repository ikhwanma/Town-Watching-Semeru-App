package com.ikhwan.townwatchingsemeru.presentation.home

import androidx.lifecycle.*
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val userUseCase: UserUseCase,
    private val pref: DataStoreManager
) : ViewModel() {

    fun getAllPosts(
        categoryId: Int?,
        level: String?,
        status: Int?
    ): LiveData<Resource<List<Post>>> =
        postUseCase.getAllPosts(categoryId, level, status).asLiveData()

    fun getCategoryUser(): LiveData<Resource<List<CategoryUser>>> =
        userUseCase.getCategoryUser().asLiveData()

    fun addLike(auth: String, postId: Int): LiveData<Resource<LikeResponse>> =
        postUseCase.addLike(auth, postId).asLiveData()

    fun getCategory(): LiveData<Resource<List<Category>>> =
        postUseCase.getAllCategory().asLiveData()

    fun getId(): LiveData<Int> {
        return pref.getId().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

}
