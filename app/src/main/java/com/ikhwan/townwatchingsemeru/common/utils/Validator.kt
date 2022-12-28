package com.ikhwan.townwatchingsemeru.common.utils

import android.net.Uri
import java.util.regex.Pattern


object Validator {
    fun postValidator(
        description: String,
        image: Uri?,
        category: String,
        level: String,
        txtStatus: String
    ): Boolean {
        return (description.isEmpty() ||
                image == null ||
                category.isEmpty() ||
                level.isEmpty() ||
                txtStatus.isEmpty() )
    }

    fun nameValidator(name: String): String{
        return if (name.isEmpty()){
            "Nama tidak boleh kosong"
        }else if(name.length < 3){
            "Nama minimal 3 karakter"
        }else{
            ""
        }
    }

    fun emailValidator(email: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun passwordValidator(password: String): String{
        return if (password.isEmpty()){
            "Password tidak boleh kosong"
        }else if(password.length < 6){
            "Password minimal 6 karakter"
        }else{
            ""
        }
    }

    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}