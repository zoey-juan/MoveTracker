package com.zoeyjuan.movetracker.ui

import android.app.Application
import android.content.Context
import com.zoeyjuan.movetracker.database.LocationDatabase
import com.zoeyjuan.movetracker.database.UserLocationDao
import com.zoeyjuan.movetracker.domain.RetrofitInteractor
import com.zoeyjuan.movetracker.reposiroty.UserLocationRepository

object Injection {

    fun provideUserDataSource(context: Context): UserLocationDao {
        val database = LocationDatabase.getInstance(context)
        return database.locationDao()
    }

    fun provideUserLocationRepository(context: Context): UserLocationRepository {
        val dataSource = provideUserDataSource(context)
        return UserLocationRepository(dataSource)
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val userLocationRepository = provideUserLocationRepository(context)
        return ViewModelFactory(context.applicationContext as Application, userLocationRepository, RetrofitInteractor())
    }
}