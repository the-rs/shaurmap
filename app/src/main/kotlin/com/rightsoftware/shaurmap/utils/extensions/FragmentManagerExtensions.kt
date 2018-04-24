package com.rightsoftware.shaurmap.utils.extensions

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

inline fun FragmentManager.transaction (func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
