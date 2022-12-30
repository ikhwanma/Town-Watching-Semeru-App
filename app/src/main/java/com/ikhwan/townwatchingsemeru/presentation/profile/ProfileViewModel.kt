package com.ikhwan.townwatchingsemeru.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.UpdateAvaResponse
import com.ikhwan.townwatchingsemeru.domain.model.User
import com.ikhwan.townwatchingsemeru.domain.use_case.user.GetUserUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.LoginUserUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UpdateAvaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateAvaUseCase: UpdateAvaUseCase,
    private val pref: DataStoreManager
): ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    suspend fun getUser(auth: String): LiveData<Resource<User>> =
        getUserUseCase.invoke(auth).asLiveData()

    fun setId(id: Int){
        viewModelScope.launch {
            pref.setId(id)
        }
    }

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    suspend fun updateAva(auth: String, image: MultipartBody.Part): LiveData<Resource<UpdateAvaResponse>> =
        updateAvaUseCase.invoke(auth, image).asLiveData()
}