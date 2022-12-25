package com.ikhwan.townwatchingsemeru.common.utils

import android.net.Uri


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
}