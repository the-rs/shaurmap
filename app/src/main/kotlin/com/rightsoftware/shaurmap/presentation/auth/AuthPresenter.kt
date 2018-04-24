package com.rightsoftware.shaurmap.presentation.auth

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.interactor.auth.AuthInteractor
import io.reactivex.Observable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val googleSignInClient: GoogleSignInClient
) : MvpPresenter<AuthView>() {

    fun startFieldsValidation(
            emailTextObservable: Observable<TextViewTextChangeEvent>,
            passwordTextObservable: Observable<TextViewTextChangeEvent>){
        authInteractor.startFieldsValidation(
                emailTextObservable
                        .map { it.text().toString() },
                passwordTextObservable
                        .map { it.text().toString() }
        )
                .subscribe(viewState::setSignButtonsEnabled)
    }

    fun signIn(email: String, password: String){
        authInteractor.signIn(email, password)
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    router.newRootScreen(Screens.MAIN_SCREEN)
                },{
                    it.printStackTrace()
                    viewState.showMessage(it.message!!)
                })
    }

    fun signUp(email: String, password: String){
        authInteractor.signUp(email, password)
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    router.newRootScreen(Screens.MAIN_SCREEN)
                },{
                    it.printStackTrace()
                    viewState.showMessage(it.message!!)
                })
    }

    fun signInWithGoogle() = viewState.chooseGoogleAccount(googleSignInClient.signInIntent)

    fun onGoogleAccountChosen(intent: Intent){
        authInteractor.signInWithGoogleAccount(intent)
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    router.newRootScreen(Screens.MAIN_SCREEN)
                },{
                    it.printStackTrace()
                    viewState.showMessage(it.message!!)
                })
    }
}