package com.rightsoftware.shaurmap

import android.app.Application
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.di.modules.AppModule
import com.rightsoftware.shaurmap.di.modules.BusinessModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

// disable verbose logging
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        configureToothpick()
        initAppScopes()
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