package com.zoeyjuan.movetracker.reposiroty

import android.arch.lifecycle.LiveData
import com.zoeyjuan.movetracker.database.UserLocation
import com.zoeyjuan.movetracker.database.UserLocationDao

class UserLocationRepository(private val dataSource: UserLocationDao) {
    fun getAllLocation(): LiveData<List<UserLocation>> = dataSource.getAll()
    fun getLastTrack(): List<UserLocation> = dataSource.getLastTrack()
    fun saveLocations(locations: MutableList<UserLocation>) {
            locations.forEach({
                dataSource.insertUserLocation(
                        location = UserLocation(it.trackTime, it.timeStamp, it.latitude, it.longtitude))
            })
    }
}