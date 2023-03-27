package com.ikhwan.townwatchingsemeru.presentation.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.ResendCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resendCodeUseCase: ResendCodeUseCase
) : ViewModel(){
    fun sendCode(resendCodeBody: ResendCodeBody): LiveData<Resource<RegisterResponse>> =
        resendCodeUseCase.invoke(resendCodeBody).asLiveData()
}