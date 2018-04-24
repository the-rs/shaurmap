package com.rightsoftware.shaurmap.presentation.profile

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.rightsoftware.shaurmap.Screens
import com.rightsoftware.shaurmap.business.LocalUser
import com.rightsoftware.shaurmap.business.interactor.profile.ProfileInteractor
import com.rightsoftware.shaurmap.data.utils.extensions.toByteArray
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor(
        private val profileInteractor: ProfileInteractor,
        private val router: Router
): MvpPresenter<ProfileView>() {

    companion object {
        private const val CHANGE_DISPLAY_NAME_RESULT_CODE = 0
    }

    init{
        router.setResultListener(CHANGE_DISPLAY_NAME_RESULT_CODE, {
            viewState.setDisplayName(it as String)
        })
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        profileInteractor.getUserInfo()
                .subscribe(this::displayProfileData)
    }

    private fun displayProfileData(localUser: LocalUser) {
        with(localUser){
            with(viewState){
                setProfilePicture(imageSource)
                setEmail(email)
                setDisplayName(displayName!!)
            }
        }
    }

    fun changeDisplayName(){
        router.navigateTo(Screens.CHANGE_DISPLAY_NAME_SCREEN, CHANGE_DISPLAY_NAME_RESULT_CODE)
    }

    fun performLogout() = viewState.showLogoutConfirmationDialog()

    fun onLogoutConfirmed(){
        profileInteractor.logout()
        router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun updateProfilePicture() = viewState.showChoosePictureSourceDialog()

    fun takePhotoFromCamera() = viewState.takePhotoFromCamera()

    fun chooseImageFromGallery() = viewState.pickImageFromGallery()

    fun deleteProfilePicture() {
        profileInteractor.deleteProfilePicture()
                .subscribe(viewState::setDefaultProfilePicture)
    }

    fun onPictureChosen(bitmap: Bitmap){
        val pictureBytes = bitmap.toByteArray()

        profileInteractor.updateProfilePicture(pictureBytes)
                { viewState.setPictureUploadProgress(it) }
                .doOnSubscribe { viewState.showPictureUploadProgress() }
                .subscribe({
                    viewState.setProfilePicture(it)
                },{
                    viewState.showError(it.message!!)
                })
    }
}
