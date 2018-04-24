package com.rightsoftware.shaurmap.data.profile.cache

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser
import com.rightsoftware.shaurmap.business.ImageSource
import javax.inject.Inject

class ProfilePictureMetadataCache @Inject constructor(
        private val sharedPreferences: SharedPreferences){
    companion object {
        private const val KEY_LAST_MODIFIED = "ProfilePictureMetadataCache.LAST_MODIFIED"
    }

    var imageLastModified : Long
        get()  = sharedPreferences.getLong(KEY_LAST_MODIFIED, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_LAST_MODIFIED, value).apply()
}