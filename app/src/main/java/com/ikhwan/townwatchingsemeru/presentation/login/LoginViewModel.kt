package com.ikhwan.townwatchingsemeru.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.local.DataStoreManager
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.login.LoginBody
import com.ikhwan.townwatchingsemeru.domain.model.PostLoginResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val pref: DataStoreManager
): ViewModel() {

    fun loginUser(user: LoginBody): LiveData<Resource<PostLoginResponse>> =
        userUseCase.loginUser(user).asLiveData()

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun setId(id: Int){
        viewModelScope.launch {
            pref.setId(id)
        }
    }
}