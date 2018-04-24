package com.rightsoftware.shaurmap.business

enum class AuthState { Authorized, Unauthorized }

data class LocalUser(
        val email: String,
        val displayName: String?,
        val imageSource: ImageSource)

class ImageSource(
        val imageUrl: String,
        val thumbnailUrl: String,
        val lastModifiedTime: Long,
        val imageBytes: ByteArray? = null)

data class Restaurant(
        val id: String,
        val latitude : Double,
        val longitude : Double)

data class User(
        val uid: String,
        val email: String,
        val displayName: String,
        val photoUrl: String,
        val thumbnailUrl: String)

// todo dislike/like enum
/*enum class Opinion constructor(val value: Int){
    Dislike(0),
    Like(1)
}*/

data class Review(
        val user: User,
        val date: Long,
        val rating: Int,
        val taste: Int?,
        val service: Int?
)

data class SubmitReview(
        val restaurantId: String,
        val rating: Int,
        val taste: Int?,
        val service: Int?
)

data class Comment(
        val text: String,
        val date: Long,
        val user: User
)

data class SubmitComment(
        val restaurantId: String,
        val text: String
)

data class RestaurantDetails(
        val creator: User,
        val createdDate: Long,
        val myReview: Review?,
        val avgRating : Double,
        val fiveStarsReviews: Int,
        val fourStarsReviews: Int,
        val threeStarsReviews: Int,
        val twoStarsReviews: Int,
        val oneStarReviews: Int,
        val tasteLikes: Int,
        val tasteDislikes: Int,
        val serviceLikes: Int,
        val serviceDislikes: Int,
        val totalReviewsCount : Int,
        val commentsCount: Int,
        val isInFavorites: Boolean)

data class FavoriteRestaurant(
        val restaurant: Restaurant,
        val addedToFavoritesDate: Long,
        val mapPictureUrl: String)
