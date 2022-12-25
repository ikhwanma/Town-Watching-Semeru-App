package com.ikhwan.townwatchingsemeru.domain.use_case.user

import com.ikhwan.townwatchingsemeru.common.Resource
import com.ikhwan.townwatchingsemeru.data.remote.dto.user.categoryuser.toCategoryUser
import com.ikhwan.townwatchingsemeru.domain.model.CategoryUser
import com.ikhwan.townwatchingsemeru.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserUseCase@Inject constructor(
    private val repository: UserRepository
) {
    fun getCategoryUser(): Flow<Resource<List<CategoryUser>>> = flow {
        try {
            emit(Resource.Loading())
            val listCategoryUser = repository.getCategoryUser().map { it.toCategoryUser() }
            emit(Resource.Success(listCategoryUser))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server, check connection"))
        }
    }
}