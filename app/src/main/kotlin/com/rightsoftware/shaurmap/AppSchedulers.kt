package com.rightsoftware.shaurmap

import com.rightsoftware.shaurmap.business.SchedulersProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppSchedulers : SchedulersProvider {
    override fun ui() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
    override fun computatution() = Schedulers.computation()
}