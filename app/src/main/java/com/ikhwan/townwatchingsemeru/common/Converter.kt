package com.ikhwan.townwatchingsemeru.common

import android.content.Context
import android.location.Geocoder
import java.util.*

object Converter {

    fun convertDate(date: String?): String {

        val arrDate = date!!.split(" ")

        val tempDate = arrDate[0].split("-")
        val tempTime = arrDate[1]

        val day = tempDate[2]
        val month = convertMonth(tempDate[1])
        val year = tempDate[0]

        return "$day $month $year $tempTime"
    }

    fun convertLocation(context: Context, lat: Double, lng: Double): String {
        val sBAddress: StringBuilder

        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)

        return addresses!![0].getAddressLine(0)
    }

    private fun convertMonth(month: String): String {
        return when (month) {
            "01" -> "Januari"
            "02" -> "Februari"
            "03" -> "Maret"
            "04" -> "April"
            "05" -> "Mei"
            "06" -> "Juni"
            "07" -> "Juli"
            "08" -> "Agustus"
            "09" -> "September"
            "10" -> "Oktober"
            "11" -> "November"
            else -> "Desember"
        }
    }
}