package com.rightsoftware.shaurmap.presentation.auth

import android.content.Intent
import com.arellomobile.mvp.MvpView

interface AuthView : MvpView {
    fun setSignButtonsEnabled(enabled: Boolean)
    fun showMessage(message: String)
    fun showProgress()
    fun hideProgress()
    fun chooseGoogleAccount(intent: Intent)
}