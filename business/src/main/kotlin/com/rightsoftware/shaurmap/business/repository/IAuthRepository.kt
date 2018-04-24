package com.rightsoftware.shaurmap.business.repository

import com.rightsoftware.shaurmap.business.AuthState
import com.rightsoftware.shaurmap.business.LocalUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface IAuthRepository{
    fun getAuthState() : Single<AuthState>
    fun signIn(email: String, password: String) : Completable
    fun signInWithGoogleAccount(data: Any) : Completable
    fun createUser(email: String, password: String) : Completable
    fun setInitialDisplayName(displayName: String) : Completable
    fun sendEmailVerification() : Completable
    fun syncProfileData() : Completable
}