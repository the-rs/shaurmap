package com.rightsoftware.shaurmap

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.di.modules.AppModule
import com.rightsoftware.shaurmap.di.modules.BusinessModule
import io.fabric.sdk.android.Fabric
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

// disable verbose logging
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initCrashlytics()
        configureToothpick()
        initAppScopes()
    }

    private fun initCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        Fabric.with(this, crashlyticsKit)
    }

    private fun configureToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(com.rightsoftware.shaurmap.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(com.rightsoftware.shaurmap.MemberInjectorRegistry())
        }
    }

    private fun initAppScopes(){
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))

        val businessScope = Toothpick.openScopes(DI.APP_SCOPE, DI.BUSINESS_SCOPE)
        businessScope.installModules(BusinessModule(
                BuildConfig.REST_API_URL, BuildConfig.GOOGLE_WEB_CLIENT_ID))
    }
}