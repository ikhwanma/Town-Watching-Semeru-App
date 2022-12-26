package com.ikhwan.townwatchingsemeru.presentation.profile.my_like

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.Like
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyLikeViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val pref: DataStoreManager
): ViewModel() {
    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getUserLike(auth: String): LiveData<Resource<List<Like>>> =
        postUseCase.getUserLike(auth).asLiveData()
}