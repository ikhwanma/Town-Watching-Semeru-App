package com.ikhwan.townwatchingsemeru.common.utils

import android.app.AlertDialog
import android.content.Context
import com.ikhwan.townwatchingsemeru.R

class ShowActionAlertDialog(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val positiveButtonAction: (() -> Unit?)? = null,
    private val negativeButtonAction: (() -> Unit?)? = null
) {
    operator fun invoke(){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.app_logo)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Iya") { _, _ ->
                positiveButtonAction?.invoke()
            }.setNegativeButton("Tidak") { _, _ ->
                negativeButtonAction?.invoke()
            }.show()
    }
}