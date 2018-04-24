package com.rightsoftware.shaurmap.business.interactor.auth

import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.extensions.isValidEmail
import com.rightsoftware.shaurmap.business.repository.IAuthRepository
import com.rightsoftware.shaurmap.business.repository.IProfileRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject

class AuthInteractor @Inject constructor(
        private val authRepository: IAuthRepository,
        private val schedulers: SchedulersProvider) {
    fun getAuthState() = authRepository.getAuthState()

    fun startFieldsValidation(
            emailTextObservable: Observable<String>,
            passwordTextObservable: Observable<String>): Observable<Boolean> {

        return Observables.combineLatest(
                emailTextObservable,
                passwordTextObservable,
                { email, password -> validateFields(email, password) })
                .distinctUntilChanged()
    }

    fun signIn(email: String, password: String): Completable {
        return authRepository.signIn(email, password)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun signInWithGoogleAccount(data: Any): Completable {
        return authRepository.signInWithGoogleAccount(data)
                .andThen(syncProfileData())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    fun signUp(email: String, password: String): Completable {
        return authRepository.createUser(email, password)
                .andThen(Completable.merge(getAuthCompletables(email)))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    private fun validateFields(email: String, password: String) =
            email.isValidEmail() && password.length > 5

    private fun getAuthCompletables(email: String): Iterable<Completable> {
        return listOf(
                authRepository.sendEmailVerification(),
                authRepository.setInitialDisplayName(email.split('@')[0])
                        .concatWith(syncProfileData())
        )

    }

    private fun syncProfileData() = authRepository.syncProfileData().subscribeOn(schedulers.io())
}