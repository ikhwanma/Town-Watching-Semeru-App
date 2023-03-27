package com.ikhwan.townwatchingsemeru.common.utils

import android.app.AlertDialog
import android.content.Context
import com.ikhwan.townwatchingsemeru.R

class ShowActionAlertDialog(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val positiveButtonText: String = "Iya",
    private val negativeButtonText: String = "Tidak",
    private val positiveButtonAction: (() -> Unit?)? = null,
    private val negativeButtonAction: (() -> Unit?)? = null,
) {
    operator fun invoke(){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.app_logo)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                positiveButtonAction?.invoke()
            }.setNegativeButton(negativeButtonText) { _, _ ->
                negativeButtonAction?.invoke()
            }.show()
    }
}