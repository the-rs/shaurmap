package com.rightsoftware.shaurmap.di.providers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject
import javax.inject.Provider

class UserStorageRefProvider @Inject constructor(
        private val storage: FirebaseStorage,
        private val auth: FirebaseAuth
) : Provider<StorageReference> {
    override fun get() = storage.reference.child("u/${auth.currentUser!!.uid}/")
}