package com.rightsoftware.shaurmap.ui.map

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import com.androidmapsextensions.GoogleMap
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.business.Restaurant
import com.rightsoftware.shaurmap.di.DI
import com.rightsoftware.shaurmap.presentation.map.MapPresenter
import com.rightsoftware.shaurmap.presentation.map.MapView
import com.rightsoftware.shaurmap.ui.global.base.BaseMapFragment
import com.rightsoftware.shaurmap.utils.extensions.*
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import kotlinx.android.synthetic.main.fragment_map.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import permissions.dispatcher.*
import toothpick.Toothpick


// todo save instance
@RuntimePermissions
class MapFragment : BaseMapFragment(), MapView {
    override val layoutRes = R.layout.fragment_map

    @InjectPresenter
    lateinit var presenter: MapPresenter

    @ProvidePresenter
    fun providePresenter(): MapPresenter {
        return Toothpick
                .openScope(DI.BUSINESS_SCOPE)
                .getInstance(MapPresenter::class.java)
    }

    private lateinit var googleMap: GoogleMap
    private val snackProgressBarManager by lazy {
        SnackProgressBarManager(view!!).setProgressBarColor(R.color.colorPrimary)
    }

    private val animFabOpen by lazy { loadAnim(R.anim.fab_open) }
    private val animFabClose by lazy { loadAnim(R.anim.fab_close) }
    private val animRotateForward by lazy { loadAnim(R.anim.fab_rotate_forward) }
    private val animRotateBackward by lazy { loadAnim(R.anim.fab_rotate_backward) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animRotateBackward.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation) {
                fabApply.visibility = View.GONE
                ivNewRestaurantMarker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
        })

        fabAddCancel.setOnClickListener {
            presenter.onAddCancelClick(fabApply.visibility == View.VISIBLE)
        }

        fabApply.setOnClickListener { confirmAddRestaurant() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.run {
            setOnMarkerClickListener {
                // todo focus if cluster
                // todo snackbar confirmation
                if (!it.isCluster) presenter.showRestaurantInfo(it.getData())
                else animateCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }

            setOnMyLocationButtonClickListener {
                presenter.onMyLocationButtonClicked()
                false
            }
        }

        super.onMapReady(googleMap)
    }

    override fun showTurnOnLocationConfirmation() {
        alert(R.string.enable_location) {
            yesButton { presenter.turnOnLocation() }
            noButton {  }
        }.show()
    }

    override fun enableLocation(){
        enableMyLocationWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun enableMyLocation(){
        googleMap.isMyLocationEnabled = true
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest){
        alert(R.string.permission_location_rationale) {
            yesButton { request.proceed() }
            noButton { request.cancel() }
        }.show()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationPermissionDenied() {
        toast(R.string.permissions_location_denied)
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onNeverAskLocationPermission(){
        toast(R.string.permission_can_be_changed_in_settings)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun addRestaurantsToMap(restaurants: List<Restaurant>) {
        googleMap.addRestaurants(restaurants)
    }

    override fun enterAddNewRestaurantMode() {
        toolbar.setTitle(R.string.title_add_restaurant)
        fabAddCancel.startAnimation(animRotateForward)

        fabApply.visibility = View.VISIBLE
        fabApply.startAnimation(animFabOpen)

        ivNewRestaurantMarker.visibility = View.VISIBLE
        ivNewRestaurantMarker.startAnimation(animFabOpen)

        fabAddCancel.animateChangeColor(getResColor(R.color.colorAccent),
                getResColor(R.color.lightRed), 500)
    }

    override fun exitAddNewRestaurantMode() {
        toolbar.setTitle(R.string.app_name)
        fabAddCancel.startAnimation(animRotateBackward)
        fabApply.startAnimation(animFabClose)
        ivNewRestaurantMarker.startAnimation(animFabClose)

        fabAddCancel.animateChangeColor(getResColor(R.color.lightRed),
                getResColor(R.color.colorAccent), 500)
    }

    override fun showEmailIsNotVerifiedError() {
        longSnackbar(view!!, R.string.email_is_not_verified, R.string.verify) {
            presenter.sendEmailVerification()
        }
    }

    override fun onEmailVerificationSent() {
        toast(R.string.email_verification_sent)
    }

    // todo move in base class
    override fun showProgress(show: Boolean) {
        if (show) {
            snackProgressBarManager.show(
                    SnackProgressBar(SnackProgressBar.TYPE_INDETERMINATE,
                            getString(R.string.please_wait))
                            .setAllowUserInput(true), SnackProgressBarManager.LENGTH_INDEFINITE)
        } else {
            snackProgressBarManager.dismiss()
        }
    }

    override fun onNewRestaurantCreated(restaurant: Restaurant) {
        // todo drop animation
        googleMap.addRestaurant(restaurant)

        longSnackbar(view!!, R.string.restaurant_has_been_added, R.string.open) {
            presenter.showRestaurantInfo(restaurant)
        }
    }

    private fun confirmAddRestaurant() {
        alert(R.string.add_restaurant_confirmation) {
            titleResource = R.string.app_name
            yesButton { presenter.onAddOperationConfirmed(googleMap.cameraPosition.target) }
            noButton { }
        }.show()
    }
}