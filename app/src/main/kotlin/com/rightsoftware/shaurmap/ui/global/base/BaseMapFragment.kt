package com.rightsoftware.shaurmap.ui.global.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidmapsextensions.ClusteringSettings
import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.MapView
import com.androidmapsextensions.OnMapReadyCallback
import com.arellomobile.mvp.MvpDelegate
import com.rightsoftware.shaurmap.R
import com.rightsoftware.shaurmap.utils.map.ClusterOptionsProvider

abstract class BaseMapFragment : Fragment(), OnMapReadyCallback {
    private var isGoogleMapReady = false
    private var isStateAlreadySaved = false
    private val mvpDelegate: MvpDelegate<out BaseMapFragment> by lazy { MvpDelegate(this) }

    protected lateinit var mapView: MapView

    companion object {
        private const val KEY_MAP_VIEW_OUT_STATE = "mapview_state"
    }

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView) as MapView

        if (savedInstanceState == null) {
            mapView.onCreate(null)
        } else {
            mapView.onCreate(savedInstanceState.getBundle(KEY_MAP_VIEW_OUT_STATE))
        }

        isGoogleMapReady = false
        mapView.getExtendedMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        isStateAlreadySaved = false
        if (isGoogleMapReady) {
            mvpDelegate.onAttach()
        }
    }

    override fun onResume() {
        super.onResume()

        isStateAlreadySaved = false
        if (isGoogleMapReady) {
            mvpDelegate.onAttach()
        }

        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()

        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mvpDelegate.onDetach()
        mvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity.isFinishing) {
            mvpDelegate.onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isStateAlreadySaved) {
            isStateAlreadySaved = false
            return
        }

        if (isRemoving) {
            mvpDelegate.onDestroy()
        }

        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        isStateAlreadySaved = true
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()

        val bundle = Bundle()
        mapView.onSaveInstanceState(bundle)
        outState.putBundle(KEY_MAP_VIEW_OUT_STATE, bundle)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        isGoogleMapReady = true
        googleMap.setClustering(ClusteringSettings().apply {
            addMarkersDynamically(true)
            clusterOptionsProvider(ClusterOptionsProvider(resources))
            clusterSize(96.0)
        })

        mvpDelegate.onAttach()
    }
}