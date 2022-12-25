package com.ikhwan.townwatchingsemeru.domain.use_case.converter

import com.ikhwan.townwatchingsemeru.common.Converter

class ConverterDate {
    fun execute(date: String): String{
        val arrDate = date!!.split(" ")

        val tempDate = arrDate[0].split("-")
        val tempTime = arrDate[1]

        val day = tempDate[2]
        val month = convertMonth(tempDate[1])
        val year = tempDate[0]

        return "$day $month $year $tempTime"
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