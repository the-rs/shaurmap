package com.rightsoftware.shaurmap.business.interactor.profile

import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.repository.IProfileRepository
import io.reactivex.Completable
import javax.inject.Inject

class ProfileInteractor @Inject constructor(
        private val profileRepository: IProfileRepository,
        private val schedulers: SchedulersProvider) {
    fun getUserInfo() = profileRepository.getUserInfo()
    fun logout() = profileRepository.logout()

    fun updateProfilePicture(imageBytes: ByteArray, onProgressChanged: (Int) -> Unit) =
            profileRepository.updateProfilePicture(imageBytes, onProgressChanged)

    fun deleteProfilePicture() = profileRepository.deleteProfilePicture()

    fun updateDisplayName(displayName: String): Completable {
        return profileRepository.updateDisplayName(displayName)
                .andThen(syncProfileData())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    private fun syncProfileData() = profileRepository.syncProfileData().subscribeOn(schedulers.io())
}