package com.ikhwan.townwatchingsemeru.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.data.remote.dto.post.comment.CommentBody
import com.ikhwan.townwatchingsemeru.domain.model.AddCommentResponse
import com.ikhwan.townwatchingsemeru.domain.model.Comment
import com.ikhwan.townwatchingsemeru.domain.model.DeleteCommentResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.post.AddCommentUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.DeleteCommentUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetAllCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val getAllCommentUseCase: GetAllCommentUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val pref: DataStoreManager
): ViewModel() {
    fun getComment(postId: Int): LiveData<Resource<List<Comment>>> =
        getAllCommentUseCase.invoke(postId).asLiveData()

    fun addComment(auth: String, comment: CommentBody): LiveData<Resource<AddCommentResponse>> =
        addCommentUseCase.invoke(auth, comment).asLiveData()

    fun deleteComment(auth: String, id: Int): LiveData<Resource<DeleteCommentResponse>> =
        deleteCommentUseCase.invoke(auth, id).asLiveData()

    fun getId(): LiveData<Int> {
        return pref.getId().asLiveData()
    }
}