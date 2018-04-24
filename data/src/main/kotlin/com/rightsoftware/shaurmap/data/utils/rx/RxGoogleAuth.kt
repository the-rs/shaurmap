package com.rightsoftware.shaurmap.data.utils.rx

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import durdinapps.rxfirebase2.RxHandler
import io.reactivex.Maybe

// todo own rx firebase implementations
class RxGoogleAuth  {
    companion object {
        fun getSignedInAccountFromIntent(data: Intent) : Maybe<GoogleSignInAccount> =
            Maybe.create {
                RxHandler.assignOnTask(it, GoogleSignIn.getSignedInAccountFromIntent(data))
            }
    }
}