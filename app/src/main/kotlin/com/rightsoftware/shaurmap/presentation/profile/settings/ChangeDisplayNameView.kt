package com.rightsoftware.shaurmap.presentation.profile.settings

import com.arellomobile.mvp.MvpView

interface ChangeDisplayNameView : MvpView {
    fun setDisplayName(displayName: String)
    fun showProgress()
    fun hideProgress()
}