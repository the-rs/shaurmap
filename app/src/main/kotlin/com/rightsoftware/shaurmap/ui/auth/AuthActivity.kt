package com.rightsoftware.shaurmap.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.ui.global.base.BaseActivity
import com.rightsoftware.shaurmap.ui.launch.MainActivity
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Replace
import toothpick.Toothpick
import javax.inject.Inject

class AuthActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_container

    @Inject lateinit var navigationHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScopes(DI.APP_SCOPE, DI.BUSINESS_SCOPE))
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommands(arrayOf(Replace(Screens.AUTH_SCREEN, null)))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.fullScreenContainer) {

        override fun createActivityIntent(context: Context, screenKey: String?, data: Any?): Intent? = when (screenKey) {
            Screens.MAIN_SCREEN -> Intent(this@AuthActivity, MainActivity::class.java)
            else -> null
        }

        override fun createFragment(screenKey: String?, data: Any?) = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            else -> null
        }
    }
}
