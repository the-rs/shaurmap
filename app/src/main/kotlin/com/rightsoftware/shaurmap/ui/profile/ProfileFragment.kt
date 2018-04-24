package com.rightsoftware.shaurmap.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.Images.Media.getBitmap
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.signature.ObjectKey
import com.rightsoftware.shaurmap.GlideApp
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.ImageSource
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.profile.ProfilePresenter
import com.rightsoftware.shaurmap.presentation.profile.ProfileView
import com.rightsoftware.shaurmap.ui.global.base.BaseFragment
import com.rightsoftware.shaurmap.data.utils.extensions.resize
import com.rightsoftware.shaurmap.utils.extensions.setImageBytes
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.error
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.yesButton
import toothpick.Toothpick


class ProfileFragment : BaseFragment(), ProfileView {
    override val layoutRes = R.layout.fragment_profile

    companion object {
        private const val TAKE_PHOTO = 0
        private const val PICK_IMAGE = 1
    }

    @InjectPresenter
    lateinit var presenter : ProfilePresenter


    @ProvidePresenter
    fun providePresenter() : ProfilePresenter {
        return Toothpick.openScope(DI.BUSINESS_SCOPE)
                .getInstance(ProfilePresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        clDisplayName.setOnClickListener { presenter.changeDisplayName() }
        btnUpdateProfilePicture.setOnClickListener{ presenter.updateProfilePicture() }

        with(toolbar){
            inflateMenu(R.menu.menu_profile)
            setOnMenuItemClickListener {
                if(it.itemId == R.id.action_logout) presenter.performLogout()
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != RESULT_OK || data == null) return

        try{
            when(requestCode){
                TAKE_PHOTO ->
                    presenter.onPictureChosen((data.extras["data"] as Bitmap).resize(800))
                PICK_IMAGE ->
                    presenter.onPictureChosen(
                            getBitmap(activity.contentResolver, data.data).resize(800))

            }
        }
        catch(e: Exception){
            error("Failed to get picture", e)
        }
    }

    override fun setProfilePicture(imageSource: ImageSource) {
        imageSource.imageBytes?.let {
            // instant display
            civProfilePicture.setImageBytes(it)
            ivProfilePictureFullscreen.setImageBytes(it)
        }

        // load and caching
        GlideApp.with(this@ProfileFragment)
                .load(imageSource.imageUrl)
                .signature(ObjectKey(imageSource.lastModifiedTime))
                .placeholder(civProfilePicture.drawable)
                .into(civProfilePicture)

        GlideApp.with(this@ProfileFragment)
                .load(imageSource.imageUrl)
                .signature(ObjectKey(imageSource.lastModifiedTime))
                .placeholder(ivProfilePictureFullscreen.drawable)
                .into(ivProfilePictureFullscreen)

    }

    override fun setEmail(email: String) {
        tvEmail.text = email
    }

    override fun setDisplayName(displayName: String) {
        tvDisplayName.text = displayName
    }

    override fun setDefaultProfilePicture() {
        // todo animations
        civProfilePicture.setImageResource(R.drawable.avatar_placeholder_square)
        ivProfilePictureFullscreen.setImageResource(R.drawable.avatar_placeholder_square)
    }

    override fun showLogoutConfirmationDialog() {
        alert(R.string.logout_confirmation) {
            titleResource = R.string.app_name
            yesButton { presenter.onLogoutConfirmed() }
            noButton {  }
        }.show()
    }

    override fun showChoosePictureSourceDialog() {
        val options = listOf(R.string.from_camera, R.string.from_gallery, R.string.delete_photo)
                .map { getString(it) }

        selector("", options, { _, optionId ->
            when(optionId){
                0 -> presenter.takePhotoFromCamera()
                1 -> presenter.chooseImageFromGallery()
                2 -> presenter.deleteProfilePicture()
            }
        })
    }

    override fun takePhotoFromCamera() {
        startActivityForResult(Intent(ACTION_IMAGE_CAPTURE), TAKE_PHOTO)
    }

    override fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.choose_picture)), PICK_IMAGE)
    }

    override fun showPictureUploadProgress() = showSnackProgressBar(getString(R.string.uploading_image))

    override fun setPictureUploadProgress(progress: Int) = setProgress(progress)

    override fun showError(error: String) = showSnackMessage(error)
}
