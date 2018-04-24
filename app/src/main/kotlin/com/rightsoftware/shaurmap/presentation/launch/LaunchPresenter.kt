package com.rightsoftware.shaurmap.presentation.launch

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.AuthState
import com.rightsoftware.shaurmap.business.interactor.auth.AuthInteractor
import io.reactivex.rxkotlin.subscribeBy
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class LaunchPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor
) : MvpPresenter<LaunchView>() {

    override fun onFirstViewAttach() {
        authInteractor.getAuthState()
                .subscribeBy { when(it){
                    AuthState.Authorized -> viewState.initMainScreen()
                    AuthState.Unauthorized -> router.newRootScreen(Screens.AUTH_SCREEN)
                } }
    }
}