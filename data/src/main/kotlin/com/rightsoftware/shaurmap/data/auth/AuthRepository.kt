package com.rightsoftware.shaurmap.data.auth

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rightsoftware.shaurmap.business.AuthState
import com.rightsoftware.shaurmap.business.repository.IAuthRepository
import com.rightsoftware.shaurmap.data.ShaurmapApi
import com.rightsoftware.shaurmap.data.utils.extensions.getUserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val shaurmapApi: ShaurmapApi
) : IAuthRepository {
    override fun getAuthState(): Single<AuthState> {
        return Single.create {
            if (firebaseAuth.currentUser != null)
                it.onSuccess(AuthState.Authorized)
            else
                it.onSuccess(AuthState.Unauthorized)
        }
    }

    override fun signIn(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    // todo intentData validation
    override fun signInWithGoogleAccount(intentData: Any): Completable {
        return Completable.create { emitter ->
            val account = Tasks.await(GoogleSignIn.getSignedInAccountFromIntent(intentData as Intent))
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }


    override fun createUser(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun setInitialDisplayName(displayName: String) : Completable {
        return Completable.create { emitter ->
            firebaseAuth.currentUser!!.updateProfile(
                    getUserProfileChangeRequest { setDisplayName(displayName) })
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun sendEmailVerification() : Completable {
        return Completable.create { emitter ->
            firebaseAuth.currentUser!!.sendEmailVerification()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun syncProfileData() = shaurmapApi.syncProfileData()
}