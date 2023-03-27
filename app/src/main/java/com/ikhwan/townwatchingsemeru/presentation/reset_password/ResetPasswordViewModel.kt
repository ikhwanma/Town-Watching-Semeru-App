package com.ikhwan.townwatchingsemeru.presentation.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.resetpassword.ResetPasswordBody
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePasswordResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    fun resetPassword(resetPasswordBody: ResetPasswordBody): LiveData<Resource<UpdatePasswordResponse>> =
        resetPasswordUseCase.invoke(resetPasswordBody).asLiveData()
}