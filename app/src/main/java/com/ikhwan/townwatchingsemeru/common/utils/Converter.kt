package com.ikhwan.townwatchingsemeru.common.utils

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

    fun convertAddress(context: Context, lat: Double, lng: Double): String {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 2)
            return addresses!![0].getAddressLine(0)
        }catch (e: Exception){
            e.printStackTrace();  //Latitude: 9.524. Longitude: 77.855 --> Mepco
        }
        return ""
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