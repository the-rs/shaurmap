package com.rightsoftware.shaurmap.di.providers

import android.content.Context
import com.rightsoftware.shaurmap.data.MyObjectBox
import io.objectbox.BoxStore
import javax.inject.Inject
import javax.inject.Provider


class BoxStoreProvider @Inject constructor(private val context: Context) : Provider<BoxStore> {
    override fun get() =
            MyObjectBox.builder()
                    .androidContext(context)
                    .buildDefault()!!
}