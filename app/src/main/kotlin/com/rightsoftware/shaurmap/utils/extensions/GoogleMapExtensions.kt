package com.rightsoftware.shaurmap.utils.extensions


import com.androidmapsextensions.GoogleMap
import com.androidmapsextensions.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.rightsoftware.shaurmap.business.Restaurant

fun GoogleMap.addRestaurants(restaurants: List<Restaurant>){
    restaurants.forEach{
        addMarker(MarkerOptions()
                .position(LatLng(it.latitude, it.longitude)))
                .setData(it)
    }
}

fun GoogleMap.addRestaurant(restaurant: Restaurant){
   this.addMarker(MarkerOptions()
                .position(LatLng(restaurant.latitude, restaurant.longitude)))
                .setData(restaurant)
}