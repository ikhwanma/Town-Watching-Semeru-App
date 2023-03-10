package com.ikhwan.townwatchingsemeru.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context : Context) {
    suspend fun setToken(token : String){
        context.tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    fun getToken() : Flow<String> {
        return context.tokenDataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    suspend fun setId(id: Int){
        context.idDataStore.edit {
            it[ID_KEY] = id
        }
    }

    fun getId() : Flow<Int> {
        return context.idDataStore.data.map {
            it[ID_KEY] ?: 0
        }
    }

    companion object{
        private const val TOKENDATA_NAME = "token_preferences"
        private const val IDDATA_NAME = "id_preferences"

        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val ID_KEY = intPreferencesKey("id_key")

        private val Context.tokenDataStore by preferencesDataStore(
            name = TOKENDATA_NAME
        )
        private val Context.idDataStore by preferencesDataStore(
            name = IDDATA_NAME
        )
    }
}