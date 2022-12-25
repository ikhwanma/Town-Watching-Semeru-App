package com.ikhwan.townwatchingsemeru.domain.use_case.converter

import android.content.Context
import android.location.Geocoder
import java.util.*

class ConverterAddress {
    fun execute(context: Context, lat: Double, lng: Double): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)

        return addresses!![0].getAddressLine(0)
    }
}