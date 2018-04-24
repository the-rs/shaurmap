package com.rightsoftware.shaurmap.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.auth.AuthPresenter
import com.rightsoftware.shaurmap.presentation.auth.AuthView
import com.rightsoftware.shaurmap.ui.global.base.BaseFragment
import com.rightsoftware.shaurmap.utils.randomResourceFrom
import kotlinx.android.synthetic.main.fragment_auth.*
import toothpick.Toothpick

class AuthFragment : BaseFragment(), AuthView {
    override val layoutRes = R.layout.fragment_auth

    companion object {
        const val CHOOSE_GOOGLE_ACCOUNT = 0
    }

    @InjectPresenter lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter {
        return Toothpick
                .openScope(DI.BUSINESS_SCOPE)
                .getInstance(AuthPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })

        presenter.startFieldsValidation(
                RxTextView.textChangeEvents(etEmail),
                RxTextView.textChangeEvents(etPassword)
        )

        btnSignIn.setOnClickListener {
            presenter.signIn(etEmail.text.toString(), etPassword.text.toString())
        }

        btnSignUp.setOnClickListener {
            presenter.signUp(etEmail.text.toString(), etPassword.text.toString())
        }

        btnGoogleSignIn.setOnClickListener {
            presenter.signInWithGoogle()
        }

        ivShaurman.setImageResource(randomResourceFrom(
                R.drawable.shaurman,
                R.drawable.shaurman2,
                R.drawable.shaurman3))
    }

    override fun chooseGoogleAccount(intent: Intent) {
        startActivityForResult(intent, CHOOSE_GOOGLE_ACCOUNT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if(resultCode != Activity.RESULT_OK) return

        if(requestCode == CHOOSE_GOOGLE_ACCOUNT) presenter.onGoogleAccountChosen(data)
    }

    override fun setSignButtonsEnabled(enabled: Boolean) {
        btnSignIn.isEnabled = enabled
        btnSignUp.isEnabled = enabled
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showProgress() {
        setProgressVisibility(true)
    }

    override fun hideProgress() {
        setProgressVisibility(false)
    }
}