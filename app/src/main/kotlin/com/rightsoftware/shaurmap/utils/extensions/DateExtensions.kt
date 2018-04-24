package com.rightsoftware.shaurmap.utils.extensions

import android.content.Context
import java.text.DateFormat
import java.util.*

fun Long.toLocalizedMediumDateString(): String {
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(this)
}

fun Long.toLocalizedShortDateString(): String {
    return DateFormat.getDateInstance(DateFormat.SHORT).format(this)
}

fun Context.getCurrentTimestamp() = Date().time
