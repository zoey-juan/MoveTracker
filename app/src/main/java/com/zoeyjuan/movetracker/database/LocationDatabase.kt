package com.zoeyjuan.movetracker.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.content.Context

@Database(entities = [(UserLocation::class)], version = 1)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): UserLocationDao

    companion object {

        @Volatile private var INSTANCE: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        LocationDatabase::class.java, "UserLocation.db")
                        .build()
    }
}