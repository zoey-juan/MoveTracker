package com.zoeyjuan.movetracker.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.zoeyjuan.movetracker.database.UserLocation
import com.zoeyjuan.movetracker.service.LocationTrackingService.Companion.MSG_LOCATION

class LocationBroadcastReceiver(private val locations: MutableList<UserLocation>,
                                private val timeStamp: Long) : BroadcastReceiver() {
    private val TAG: String = LocationBroadcastReceiver::class.toString()

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "LocationBroadcastReceiver")

        val location = intent.getParcelableExtra<Location>(MSG_LOCATION)
        val userLocation = UserLocation(location.time, timeStamp, location.latitude, location.longitude)

        Log.i(TAG, "Lat: ${userLocation.latitude}")
        Log.i(TAG, "Lon: ${userLocation.longtitude}")
        locations.add(userLocation)
    }
}