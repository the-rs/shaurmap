package com.rightsoftware.shaurmap.presentation.profile

import com.arellomobile.mvp.MvpView
import com.rightsoftware.shaurmap.business.ImageSource

interface ProfileView : MvpView{
    fun setProfilePicture(imageSource: ImageSource)
    fun setDefaultProfilePicture()
    fun setEmail(email: String)
    fun setDisplayName(displayName: String)
    fun showLogoutConfirmationDialog()
    fun showChoosePictureSourceDialog()
    fun takePhotoFromCamera()
    fun pickImageFromGallery()
    fun showPictureUploadProgress()
    fun setPictureUploadProgress(progress: Int)
    fun showError(error: String)
}