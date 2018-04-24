package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.ImageSource
import com.rightsoftware.shaurmap.business.LocalUser
import io.reactivex.Completable
import io.reactivex.Single

interface IProfileRepository {
    fun updateProfilePicture(
            imageBytes: ByteArray,
            onProgressChanged: (Int) -> Unit): Single<ImageSource>

    fun getUserInfo() : Single<LocalUser>
    fun updateDisplayName(newDisplayName: String) : Completable
    fun syncProfileData() : Completable
    fun deleteProfilePicture() : Completable
    fun isEmailVerified() : Single<Boolean>
    fun sendEmailVerification() : Completable
    fun logout()
}