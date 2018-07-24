package com.zoeyjuan.movetracker.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "userlocations")
data class UserLocation(@PrimaryKey
                        @ColumnInfo(name = "trackTime")
                        var trackTime: Long = 0L,
                        @ColumnInfo(name = "timestamp")
                        var timeStamp: Long = 0,
                        @ColumnInfo(name = "latitude")
                        var latitude: Double = 0.0,
                        @ColumnInfo(name = "longtitude")
                        var longtitude: Double = 0.0
)