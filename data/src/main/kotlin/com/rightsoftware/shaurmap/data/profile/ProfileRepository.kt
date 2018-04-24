package com.rightsoftware.shaurmap.data.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference
import com.rightsoftware.shaurmap.business.ImageSource
import com.rightsoftware.shaurmap.business.LocalUser
import com.rightsoftware.shaurmap.business.repository.IProfileRepository
import com.rightsoftware.shaurmap.data.ShaurmapApi
import com.rightsoftware.shaurmap.data.di.qualifiers.UserStorageRef
import com.rightsoftware.shaurmap.data.profile.cache.ProfilePictureMetadataCache
import com.rightsoftware.shaurmap.data.utils.extensions.*
import durdinapps.rxfirebase2.RxFirebaseStorage
import durdinapps.rxfirebase2.RxFirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileRepository @Inject constructor(
        private val auth: FirebaseAuth,
        private val profilePictureMetadataCache: ProfilePictureMetadataCache,
        @UserStorageRef private val storageRef: StorageReference,
        private val shaurmapApi: ShaurmapApi
) : IProfileRepository {

    companion object {
        const val PROFILE_PICTURE_PATH = "profile.jpg"
        const val PROFILE_THUMBNAIL_PATH = "profile-thumbnail.jpg"
    }

    override fun getUserInfo(): Single<LocalUser> {
        return Single.just(auth.currentUser)
                .map { it.toDomainUser() }
    }

    override fun updateDisplayName(newDisplayName: String): Completable {
        return RxFirebaseUser.updateProfile(auth.currentUser!!,
                UserProfileChangeRequest.Builder().setDisplayName(newDisplayName).build())
    }

    override fun syncProfileData() = shaurmapApi.syncProfileData()

    // todo handle internet availability
    override fun updateProfilePicture(
            imageBytes: ByteArray,
            onProgressChanged: (Int) -> Unit): Single<ImageSource> {

        val sourcePictureRef = storageRef.child(PROFILE_PICTURE_PATH)
        val thumbnailRef = storageRef.child(PROFILE_THUMBNAIL_PATH)

        thumbnailRef.putBytes(
                imageBytes.toBitmap().centerCrop().resize(60).toByteArray())

        return Single.create<ImageSource> { emitter ->
            onProgressChanged(0)

            sourcePictureRef.putBytes(imageBytes)
                    .addOnProgressListener {
                        val progress = (100 * it.bytesTransferred) / it.totalByteCount
                        onProgressChanged(progress.toInt())
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
                    .addOnSuccessListener {
                        val imageUrl = it.downloadUrl.toString().split('&')[0]

                        auth.currentUser!!.updateProfile(
                                getUserProfileChangeRequest {
                                    setPhotoUri(imageUrl.toUri())
                                }
                        )

                        val imageLastModified = it.metadata!!.updatedTimeMillis
                        profilePictureMetadataCache.imageLastModified = imageLastModified

                        val imageSource = ImageSource(
                                imageUrl,
                                imageUrl.replace(PROFILE_PICTURE_PATH, PROFILE_THUMBNAIL_PATH),
                                imageLastModified,
                                imageBytes
                        )

                        emitter.onSuccess(imageSource)
                    }

        }
    }

    override fun deleteProfilePicture(): Completable {
        val sourcePictureRef = storageRef.child("profile.jpg")
        val thumbnailRef = storageRef.child("profile-thumbnail.jpg")

        return RxFirebaseStorage.delete(sourcePictureRef).mergeWith(
                RxFirebaseStorage.delete(thumbnailRef))
    }

    override fun sendEmailVerification(): Completable {
        return Completable.create { emitter ->
            auth.currentUser!!.sendEmailVerification()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun logout() = auth.signOut()

    override fun isEmailVerified(): Single<Boolean> {
        return Single.create { emitter ->
            auth.currentUser!!.reload()
                    .addOnSuccessListener { emitter.onSuccess(auth.currentUser!!.isEmailVerified) }
                    .addOnFailureListener { emitter.onError(it) }
        }
    }

    private fun FirebaseUser.toDomainUser(): LocalUser {
        return LocalUser(
                email!!,
                displayName,
                ImageSource(
                        photoUrl.toString(),
                        photoUrl.toString().replace(PROFILE_PICTURE_PATH, PROFILE_THUMBNAIL_PATH),
                        profilePictureMetadataCache.imageLastModified
                )
        )
    }
}