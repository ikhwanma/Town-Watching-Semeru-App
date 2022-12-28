package com.ikhwan.townwatchingsemeru.presentation.update_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.editprofile.UpdateProfileBody
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.model.UpdateProfileResponse
import com.ikhwan.townwatchingsemeru.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    fun getCategoryUser(): LiveData<Resource<List<CategoryUser>>> =
        userUseCase.getCategoryUser().asLiveData()

    fun updateProfile(
        auth: String,
        updateProfileBody: UpdateProfileBody
    ): LiveData<Resource<UpdateProfileResponse>> =
        userUseCase.updateProfile(auth, updateProfileBody).asLiveData()
}