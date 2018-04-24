package com.rightsoftware.shaurmap.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.profile.settings.ChangeDisplayNamePresenter
import com.rightsoftware.shaurmap.presentation.profile.settings.ChangeDisplayNameView
import com.rightsoftware.shaurmap.ui.global.SlidableActivity
import com.rightsoftware.shaurmap.utils.consume
import kotlinx.android.synthetic.main.activity_change_display_name.*
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.commands.Back
import toothpick.Toothpick
import javax.inject.Inject


class ChangeDisplayNameActivity : SlidableActivity(), ChangeDisplayNameView {
    override val layoutRes = R.layout.activity_change_display_name

    @Inject lateinit var navigationHolder: NavigatorHolder

    @InjectPresenter
    lateinit var presenter : ChangeDisplayNamePresenter

    @ProvidePresenter
    fun providePresenter() : ChangeDisplayNamePresenter {
        return Toothpick.openScope(DI.BUSINESS_SCOPE)
                .getInstance(ChangeDisplayNamePresenter::class.java)
    }

    companion object {
        private const val EXTRA_RESULT_CODE = "ChangeDisplayNameActivity.result_code"

        fun newIntent(context: Context, resultCode: Int) =
            Intent(context, ChangeDisplayNameActivity::class.java).apply {
                putExtra(EXTRA_RESULT_CODE, resultCode)
            }
    }

    private val resultCode: Int by lazy { intent.getIntExtra(EXTRA_RESULT_CODE, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toothpick.inject(this, Toothpick.openScope(DI.BUSINESS_SCOPE))

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            presenter.onBackPressed()
        }
    }

    override fun setDisplayName(displayName: String) {
        etDisplayName.setText(displayName)
        etDisplayName.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator {
            if(it is Back)
                finish()
        }
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_change_user_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        R.id.action_save ->
            consume { presenter.updateDisplayName(etDisplayName.text.toString(), resultCode) }
        R.id.home ->
            consume { presenter.onBackPressed() }

        else ->  super.onOptionsItemSelected(item)
    }

    override fun showProgress() {
        setProgressVisibility(true)
    }

    override fun hideProgress() {
        setProgressVisibility(false)
    }
}