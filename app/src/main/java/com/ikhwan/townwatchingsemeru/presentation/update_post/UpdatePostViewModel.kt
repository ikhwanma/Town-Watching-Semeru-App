package com.ikhwan.townwatchingsemeru.presentation.update_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.updatepost.UpdatePostBody
import com.ikhwan.townwatchingsemeru.domain.model.Category
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePostResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdatePostViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {
    fun updatePost(
        auth: String,
        updatePostBody: UpdatePostBody
    ): LiveData<Resource<UpdatePostResponse>> =
        postUseCase.updatePost(auth, updatePostBody).asLiveData()

    fun getCategory(): LiveData<Resource<List<Category>>> =
        postUseCase.getAllCategory().asLiveData()
}