package com.ikhwan.townwatchingsemeru.presentation.edit_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editpassword.EditPasswordBody
import com.ikhwan.townwatchingsemeru.domain.model.UpdatePasswordResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.EditPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val editPasswordUseCase: EditPasswordUseCase
) : ViewModel() {

    suspend fun editPassword(
        auth: String,
        editPassword: EditPasswordBody
    ): LiveData<Resource<UpdatePasswordResponse>> =
        editPasswordUseCase.invoke(auth, editPassword).asLiveData()

}