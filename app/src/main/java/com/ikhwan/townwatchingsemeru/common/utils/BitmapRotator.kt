package com.ikhwan.townwatchingsemeru.common.utils

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

object BitmapRotator {
    fun getRotatedBitmap(orientation: Int, bitmap: Bitmap): Bitmap{
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }

    }
}