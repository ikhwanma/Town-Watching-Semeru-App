package com.ikhwan.townwatchingsemeru.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.AddCommentResponseDto
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.domain.model.AddCommentResponse
import com.ikhwan.townwatchingsemeru.domain.model.Comment
import com.ikhwan.townwatchingsemeru.domain.use_case.post.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val postUseCase: PostUseCase
): ViewModel() {
    fun getComment(postId: Int): LiveData<Resource<List<Comment>>> =
        postUseCase.getAllComment(postId).asLiveData()

    fun addComment(auth: String, comment: CommentBody): LiveData<Resource<AddCommentResponse>> =
        postUseCase.addComment(auth, comment).asLiveData()
}