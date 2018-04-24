package com.rightsoftware.shaurmap.data.utils.extensions

import android.net.Uri

fun String.toUri() = Uri.parse(this)