package com.rightsoftware.shaurmap.data

import com.rightsoftware.shaurmap.business.*
import com.rightsoftware.shaurmap.business.Restaurant
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ShaurmapApi {
    @GET("restaurants")
    fun getRestaurants() : Single<List<Restaurant>>

    @POST("restaurants")
    fun postRestaurant(@Body restaurant: Restaurant) : Single<Restaurant>

    @GET("restaurants/{restaurantId}")
    fun getRestaurantDetails(@Path("restaurantId") restaurantId: String) : Single<RestaurantDetails>

    @GET("reviews")
    fun getReviews(
            @Query("id") restaurantId: String,
            @Query("p") page: Int,
            @Query("latest") latestReviewTimestamp: Long?) : Single<List<Review>>

    @POST("reviews")
    fun postReview(@Body review: SubmitReview) : Single<Review>

    @GET("comments")
    fun getComments(
            @Query("id") restaurantId: String,
            @Query("p") page: Int,
            @Query("latest") latestCommentTimestamp: Long?) : Single<List<Comment>>

    @POST("comments")
    fun postComment(@Body comment: SubmitComment) : Single<Comment>

    @GET("sync")
    fun syncProfileData() : Completable

    @GET("favorites")
    fun getFavoriteRestaurants(
            @Query("p") page: Int,
            @Query("latest") latestReviewTimestamp: Long?
    ) : Single<List<FavoriteRestaurant>>

    @POST("favorites/{restaurantId}")
    fun addRestaurantToFavorites(@Path("restaurantId") restaurantId: String) : Single<FavoriteRestaurant>

    @DELETE("favorites/{restaurantId}")
    fun removeRestaurantFromFavorites(@Path("restaurantId") restaurantId: String) : Completable
}