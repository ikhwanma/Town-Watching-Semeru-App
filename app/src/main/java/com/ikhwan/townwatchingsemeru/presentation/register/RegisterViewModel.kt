package com.ikhwan.townwatchingsemeru.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.RegisterBody
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.GetCategoryUserUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getCategoryUserUseCase: GetCategoryUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel(){

    suspend fun getCategoryUser(): LiveData<Resource<List<CategoryUser>>> =
        getCategoryUserUseCase.invoke().asLiveData()

    suspend fun registerUser(registerBody: RegisterBody): LiveData<Resource<RegisterResponse>> =
        registerUserUseCase.invoke(registerBody).asLiveData()
}