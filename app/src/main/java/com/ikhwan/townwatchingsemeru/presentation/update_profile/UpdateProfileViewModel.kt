package com.ikhwan.townwatchingsemeru.presentation.update_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileBody
import com.ikhwan.townwatchingsemeru.domain.model.UpdateProfileResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    fun updateProfile(
        auth: String,
        updateProfileBody: UpdateProfileBody
    ): LiveData<Resource<UpdateProfileResponse>> =
        updateProfileUseCase.invoke(auth, updateProfileBody).asLiveData()
}