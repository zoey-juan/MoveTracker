package com.zoeyjuan.movetracker.ui

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.zoeyjuan.movetracker.domain.RetrofitInteractor
import com.zoeyjuan.movetracker.reposiroty.UserLocationRepository

class ViewModelFactory(private val context: Application,
                       private val userLocationRepository: UserLocationRepository,
                       private val retrofitInteractor: RetrofitInteractor)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLocationViewModel::class.java)) {
            return UserLocationViewModel(context, userLocationRepository, retrofitInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}