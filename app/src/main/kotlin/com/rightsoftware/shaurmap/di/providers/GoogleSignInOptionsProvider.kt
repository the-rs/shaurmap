package com.rightsoftware.shaurmap.di.providers

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rightsoftware.shaurmap.di.qualifiers.GoogleWebClientId
import javax.inject.Inject
import javax.inject.Provider

class GoogleSignInOptionsProvider @Inject constructor(
        @GoogleWebClientId private val googleWebClientId: String
) : Provider<GoogleSignInOptions> {
    override fun get() : GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleWebClientId)
                .requestEmail()
                .build()
}