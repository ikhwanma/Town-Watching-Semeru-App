package com.ikhwan.townwatchingsemeru.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.domain.model.User
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val pref: DataStoreManager
): ViewModel() {

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getUser(auth: String): LiveData<Resource<User>> =
        userUseCase.getUser(auth).asLiveData()

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
}