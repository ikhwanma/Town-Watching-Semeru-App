package com.ikhwan.townwatchingsemeru.common.utils

import android.app.AlertDialog
import android.content.Context

class ShowAlertDialog(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val positiveButtonAction: () -> Unit,
    private val negativeButtonAction: () -> Unit
) {
    fun alertDialogButton() {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Iya") { _, _ ->
                positiveButtonAction()
            }.setNegativeButton("Tidak") { _, _ ->
                negativeButtonAction()
            }.show()
    }
}