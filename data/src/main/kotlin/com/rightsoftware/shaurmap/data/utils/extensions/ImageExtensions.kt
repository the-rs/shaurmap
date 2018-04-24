package com.rightsoftware.shaurmap.data.utils.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


fun Bitmap.toByteArray() : ByteArray {
    return with(ByteArrayOutputStream()){
        this@toByteArray.compress(Bitmap.CompressFormat.PNG, 90, this)
        this.toByteArray()
    }
}

fun Bitmap.resize(maxSize: Int) : Bitmap {
    var newWidth = width
    var newHeight = height

    val bitmapRatio = newWidth.toFloat() / newHeight.toFloat()
    if (bitmapRatio > 1) {
        newWidth = maxSize
        newHeight = (newWidth / bitmapRatio).toInt()
    } else {
        newHeight = maxSize
        newWidth = (newHeight * bitmapRatio).toInt()
    }

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}

fun Bitmap.centerCrop() : Bitmap {
    return if (width >= height)
        Bitmap.createBitmap(this, width/2 - height/2, 0, height, height)
    else
        Bitmap.createBitmap(this, 0, height/2 - width/2, width, width)
}

fun ByteArray.toBitmap() : Bitmap{
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}