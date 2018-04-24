package com.rightsoftware.shaurmap.di.providers

import com.rightsoftware.shaurmap.data.ShaurmapApi
import com.rightsoftware.shaurmap.di.qualifiers.ServerPath
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
        @ServerPath private val serverPath: String,
        private val okHttpClient: OkHttpClient
) : Provider<ShaurmapApi> {
    override fun get() : ShaurmapApi =
        Retrofit.Builder()
                .baseUrl(serverPath)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ShaurmapApi::class.java)
}