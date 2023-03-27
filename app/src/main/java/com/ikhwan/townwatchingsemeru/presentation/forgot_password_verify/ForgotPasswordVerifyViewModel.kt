package com.ikhwan.townwatchingsemeru.presentation.forgot_password_verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.ResendCodeBody
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.register.VerifyCodeBody
import com.ikhwan.townwatchingsemeru.domain.model.RegisterResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.ResendCodeUseCase
import com.ikhwan.townwatchingsemeru.domain.use_case.user.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVerifyViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val resendCodeUseCase: ResendCodeUseCase
) : ViewModel() {
    fun verifyCode(verifyCodeBody: VerifyCodeBody): LiveData<Resource<RegisterResponse>> =
        verifyCodeUseCase.invoke(verifyCodeBody).asLiveData()
    fun resendCode(resendCodeBody: ResendCodeBody): LiveData<Resource<RegisterResponse>> =
        resendCodeUseCase.invoke(resendCodeBody).asLiveData()
}