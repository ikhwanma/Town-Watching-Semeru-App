package com.ikhwan.townwatchingsemeru.common.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import com.google.android.material.snackbar.Snackbar

class ShowSnackbarPermission {
    fun permissionDisabled(view: View, activity: Activity){
        Snackbar.make(
            view,
            "Aplikasi memerlukan izin akses, berikan izin terlebih dahulu",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Buka Pengaturan"){
            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.packageName))
            activity.startActivity(i)
        }.show()
    }

    fun locationDisabled(view: View, activity: Activity){
        Snackbar.make(
            view,
            "Lokasi tidak diaktifkan, Aktifkan Lokasi?",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Buka Pengaturan"){
            val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(i)
        }.show()
    }
}