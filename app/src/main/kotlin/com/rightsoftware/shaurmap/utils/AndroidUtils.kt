package com.rightsoftware.shaurmap.utils

import java.util.*

inline fun networkIsAvailable(code: () -> Unit){
    /*val connectivityManager =
            App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if(connectivityManager.activeNetworkInfo?.isConnected == true)
        code()*/
}

inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun randomResourceFrom(vararg resourceIds: Int) =
        resourceIds[Random().nextInt(resourceIds.size)]