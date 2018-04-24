package com.rightsoftware.shaurmap.di.providers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject
import javax.inject.Provider

class GoogleSignInClientProvider @Inject constructor(
      private val context: Context,
      private val googleSignInOptions: GoogleSignInOptions
) : Provider<GoogleSignInClient> {
    override fun get(): GoogleSignInClient =
            GoogleSignIn.getClient(context, googleSignInOptions)
}