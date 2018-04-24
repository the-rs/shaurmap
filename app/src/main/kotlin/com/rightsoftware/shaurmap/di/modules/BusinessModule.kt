package com.rightsoftware.shaurmap.di.modules

import android.content.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rightsoftware.shaurmap.AppSchedulers
import com.rightsoftware.shaurmap.business.SchedulersProvider
import com.rightsoftware.shaurmap.business.interactor.auth.AuthInteractor
import com.rightsoftware.shaurmap.business.interactor.favorites.FavoritesInteractor
import com.rightsoftware.shaurmap.business.interactor.map.MapInteractor
import com.rightsoftware.shaurmap.business.interactor.profile.ProfileInteractor
import com.rightsoftware.shaurmap.business.repository.*
import com.rightsoftware.shaurmap.data.ShaurmapApi
import com.rightsoftware.shaurmap.data.auth.AuthRepository
import com.rightsoftware.shaurmap.data.device.DeviceSettingsRepository
import com.rightsoftware.shaurmap.data.di.qualifiers.UserStorageRef
import com.rightsoftware.shaurmap.data.favorites.FavoritesRepository
import com.rightsoftware.shaurmap.data.profile.ProfileRepository
import com.rightsoftware.shaurmap.data.restaurants.RestaurantsRepository
import com.rightsoftware.shaurmap.data.restaurants.cache.RestaurantsCache
import com.rightsoftware.shaurmap.data.utils.mappers.DomainDbMapper
import com.rightsoftware.shaurmap.di.providers.*
import com.rightsoftware.shaurmap.di.qualifiers.GoogleWebClientId
import com.rightsoftware.shaurmap.di.qualifiers.ServerPath
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import toothpick.config.Module

// todo fix provider instances
class BusinessModule(serverUrl: String, googleWebClientId: String) : Module() {
    init {
        bind(ShaurmapApi::class.java).toProvider(ApiProvider::class.java).singletonInScope()
        bind(OkHttpClient::class.java)
                .toProvider(OkHttpProvider::class.java)
                .singletonInScope()

        bind(BoxStore::class.java).toProvider(BoxStoreProvider::class.java).singletonInScope()
        bind(RestaurantsCache::class.java).singletonInScope()


        // Map
        bind(IRestaurantsRepository::class.java).to(RestaurantsRepository::class.java)
        bind(MapInteractor::class.java)

        bind(DomainDbMapper::class.java).toInstance(DomainDbMapper())

        // Auth
        bind(IAuthRepository::class.java).to(AuthRepository::class.java)
        bind(AuthInteractor::class.java)

        // Profile
        bind(IProfileRepository::class.java).to(ProfileRepository::class.java)
        bind(ProfileInteractor::class.java)

        // Favorites
        bind(IFavoritesRepository::class.java).to(FavoritesRepository::class.java)
        bind(FavoritesInteractor::class.java)

        bind(IDeviceSettingsRepository::class.java).to(DeviceSettingsRepository::class.java)

        bind(String::class.java).withName(ServerPath::class.java).toInstance(serverUrl)
        bind(String::class.java).withName(GoogleWebClientId::class.java).toInstance(googleWebClientId)

        bind(FirebaseAuth::class.java).toInstance(FirebaseAuth.getInstance())
        bind(FirebaseStorage::class.java).toInstance(FirebaseStorage.getInstance())

        bind(GoogleSignInOptions::class.java).toProvider(GoogleSignInOptionsProvider::class.java)
        bind(GoogleSignInClient::class.java).toProvider(GoogleSignInClientProvider::class.java)
        bind(StorageReference::class.java).withName(UserStorageRef::class.java)
                .toProvider(UserStorageRefProvider::class.java)

        bind(SharedPreferences::class.java).toProvider(SharedPreferencesProvider::class.java)
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
    }
}