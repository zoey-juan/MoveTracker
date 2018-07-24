package com.zoeyjuan.movetracker.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface UserLocationDao {
    @Query("SELECT * FROM userlocations")
    fun getAll(): LiveData<List<UserLocation>>

    // TODO: FIX wrong query
    @Query("SELECT * FROM userlocations")
    fun getLastTrack(): List<UserLocation>

    @Insert
    fun insertAll(vararg users: UserLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserLocation(location: UserLocation)

    @Delete
    fun delete(user: UserLocation)
}