package com.rightsoftware.shaurmap.ui.launch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.di.modules.RestaurantDetailsModule
import com.rightsoftware.shaurmap.presentation.launch.LaunchPresenter
import com.rightsoftware.shaurmap.presentation.launch.LaunchView
import com.rightsoftware.shaurmap.ui.MainFragment
import com.rightsoftware.shaurmap.ui.auth.AuthActivity
import com.rightsoftware.shaurmap.ui.global.base.BaseActivity
import com.rightsoftware.shaurmap.ui.profile.ChangeDisplayNameActivity
import com.rightsoftware.shaurmap.ui.restaurant.RestaurantDetailsActivity
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject
import com.rightsoftware.shaurmap.di.qualifiers.Restaurant as RestaurantQualifier


class MainActivity : BaseActivity(), LaunchView {
    override val layoutRes = R.layout.activity_container

    @InjectPresenter
    lateinit var presenter: LaunchPresenter

    @Inject
    lateinit var navigationHolder: NavigatorHolder

    @ProvidePresenter
    fun providePresenter(): LaunchPresenter {
        return Toothpick
                .openScope(DI.BUSINESS_SCOPE)
                .getInstance(LaunchPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        Toothpick.inject(this, Toothpick.openScope(DI.BUSINESS_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun initMainScreen() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fullScreenContainer, MainFragment())
                .commitNow()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.fullScreenContainer) {
        override fun createActivityIntent(context: Context, screenKey: String?, data: Any?): Intent? = when (screenKey) {
            Screens.AUTH_SCREEN -> {
                Intent(this@MainActivity, AuthActivity::class.java)
            }

            Screens.CHANGE_DISPLAY_NAME_SCREEN -> {
                ChangeDisplayNameActivity.newIntent(this@MainActivity, data as Int)
            }

            Screens.RESTAURANT_DETAILS_SCREEN -> {
                Toothpick.openScopes(DI.BUSINESS_SCOPE, DI.RESTAURANT_DETAILS_SCOPE)
                        .installModules(
                                RestaurantDetailsModule(),
                                object : Module() {
                                    init {
                                        bind(Restaurant::class.java)
                                                .withName(RestaurantQualifier::class.java)
                                                .toInstance(data as Restaurant)
                                    }
                                })

                Intent(this@MainActivity, RestaurantDetailsActivity::class.java)
            }

            Screens.LOCATION_SETTINGS_SCREEN -> {
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            }
            else -> null
        }

        override fun createFragment(screenKey: String?, data: Any?) = when (screenKey) {
            Screens.MAIN_SCREEN -> MainFragment()
            else -> null
        }
    }
}