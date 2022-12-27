package com.ikhwan.townwatchingsemeru.presentation.detail_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.DeletePostResponse
import com.ikhwan.townwatchingsemeru.domain.model.LikeResponse
import com.ikhwan.townwatchingsemeru.domain.model.Post
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel @Inject constructor(
    private val postUseCase: PostUseCase,
    private val pref: DataStoreManager
):ViewModel() {

    fun getDetailPost(id: Int): LiveData<Resource<Post>> =
        postUseCase.getDetailPost(id).asLiveData()

    fun getId() : LiveData<Int> {
        return pref.getId().asLiveData()
    }

    fun addLike(auth: String, postId: Int): LiveData<Resource<LikeResponse>> =
        postUseCase.addLike(auth, postId).asLiveData()

    fun getToken() : LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun deletePostUser(auth: String, id: Int): LiveData<Resource<DeletePostResponse>> =
        postUseCase.deleteUserPost(auth, id).asLiveData()
}