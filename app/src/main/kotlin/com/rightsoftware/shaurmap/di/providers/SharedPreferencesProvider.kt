package com.rightsoftware.shaurmap.di.providers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Provider

class SharedPreferencesProvider @Inject constructor(
        private val context: Context
) : Provider<SharedPreferences>{
    override fun get() = PreferenceManager.getDefaultSharedPreferences(context)
}