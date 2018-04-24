package com.rightsoftware.shaurmap.utils.extensions

import com.google.android.gms.maps.model.LatLng
import com.rightsoftware.shaurmap.business.Restaurant

val Restaurant.position
    get() = LatLng(latitude, longitude)