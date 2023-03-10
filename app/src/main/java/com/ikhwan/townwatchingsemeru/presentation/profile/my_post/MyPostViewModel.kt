package com.ikhwan.townwatchingsemeru.presentation.profile.my_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.use_case.post.DeleteUserPostUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetUserPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val getUserPostUseCase: GetUserPostUseCase,
    private val pref: DataStoreManager
): ViewModel() {
    fun getUserPost(auth: String): LiveData<Resource<List<Post>>> =
        getUserPostUseCase.invoke(auth).asLiveData()

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}