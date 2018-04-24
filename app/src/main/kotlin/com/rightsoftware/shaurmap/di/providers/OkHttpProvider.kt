package com.rightsoftware.shaurmap.di.providers

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider

class OkHttpProvider @Inject constructor(
        private val firebaseAuth: FirebaseAuth
) : Provider<OkHttpClient> {
    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val tokenResult = Tasks.await(firebaseAuth.currentUser!!.getIdToken(true))
                    val token = tokenResult.token!!

                    val modifiedRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()

                    chain.proceed(modifiedRequest)
                }
                .build()
    }
}