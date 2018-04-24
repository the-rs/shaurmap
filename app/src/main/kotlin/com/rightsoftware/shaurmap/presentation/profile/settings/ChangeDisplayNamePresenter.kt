package com.rightsoftware.shaurmap.presentation.profile.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.business.interactor.profile.ProfileInteractor
import io.reactivex.rxkotlin.subscribeBy
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ChangeDisplayNamePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor,
        private val router: Router
): MvpPresenter<ChangeDisplayNameView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        profileInteractor.getUserInfo()
                .subscribeBy { viewState.setDisplayName(it.displayName!!) }
    }

    fun updateDisplayName(newDisplayName: String, resultCode: Int){
        profileInteractor.updateDisplayName(newDisplayName)
                .doOnSubscribe{ viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    router.exitWithResult(resultCode, newDisplayName)
                },{
                    // todo error processing
                    it.printStackTrace()
                })

    }

    fun onBackPressed() = router.exit()
}