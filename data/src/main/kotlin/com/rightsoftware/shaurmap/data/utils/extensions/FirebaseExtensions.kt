package com.rightsoftware.shaurmap.data.utils.extensions

import com.google.firebase.auth.UserProfileChangeRequest

fun getUserProfileChangeRequest(
        func: UserProfileChangeRequest.Builder.() -> UserProfileChangeRequest.Builder
) : UserProfileChangeRequest{
    return UserProfileChangeRequest.Builder().func().build()
}