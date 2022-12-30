package com.ikhwan.townwatchingsemeru.presentation.profile.my_like

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.Like
import com.ikhwan.townwatchingsemeru.domain.use_case.post.AddLikeUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.post.GetUserLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyLikeViewModel @Inject constructor(
    private val getUserLikeUseCase: GetUserLikeUseCase,
    private val pref: DataStoreManager
): ViewModel() {
    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    suspend fun getUserLike(auth: String): LiveData<Resource<List<Like>>> =
        getUserLikeUseCase.invoke(auth).asLiveData()
}