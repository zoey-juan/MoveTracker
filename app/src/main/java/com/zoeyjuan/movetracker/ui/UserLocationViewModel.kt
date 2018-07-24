package com.zoeyjuan.movetracker.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.zoeyjuan.movetracker.database.UserLocation
import android.arch.lifecycle.LiveData
import com.zoeyjuan.movetracker.domain.RetrofitInteractor
import com.zoeyjuan.movetracker.reposiroty.UserLocationRepository
import io.reactivex.Completable

class UserLocationViewModel(application: Application,
                            userLocationRepository: UserLocationRepository,
                            retrofitInteractor: RetrofitInteractor) : AndroidViewModel(application) {

    private val userLocationRepo = userLocationRepository
    private val retrofitInteractor = retrofitInteractor

    fun getAllLocation(): LiveData<List<UserLocation>> {
        return userLocationRepo.getAllLocation()
    }

    fun updateUserLocation(): Completable {
        return Completable.fromAction {
            retrofitInteractor.uploadTrack(userLocationRepo.getLastTrack())
        }
    }
}