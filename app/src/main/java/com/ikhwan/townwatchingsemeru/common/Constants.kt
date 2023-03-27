package com.ikhwan.townwatchingsemeru.common

import com.ikhwan.townwatchingsemeru.R

object Constants {
    const val BASE_URL = ""
    const val EXTRA_ID = "extra_id"
    const val EXTRA_TOKEN = "extra_token"
    const val EXTRA_NAME = "extra_name"
    const val EXTRA_EMAIL = "extra_email"
    const val EXTRA_CATEGORY = "extra_category"
    const val EXTRA_LEVEL = "extra_level"
    const val EXTRA_IMAGE = "extra_image"
    const val EXTRA_DESCRIPTION = "extra_description"
    const val EXTRA_STATUS = "extra_status"
    const val EXTRA_FRAGMENT = "extra_fragment"
    const val EXTRA_DETAIL_BENCANA = "extra_detail_bencana"

    val listLevel = mutableListOf("Ringan", "Sedang", "Berat")
    val listStatus = mutableListOf("Aktif", "Tidak Aktif")
    val listDetailBencana =
        mutableListOf("Erupsi", "Gempa Bumi", "Tanah Longsor", "Banjir", "Lainnya")
    val listImageCategory =
        mutableListOf(
            R.drawable.ic_volcano,
            R.drawable.ic_earthquake,
            R.drawable.ic_landslide,
            R.drawable.ic_flood
        )
}