package com.zoeyjuan.movetracker.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.util.*
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.zoeyjuan.movetracker.ui.Injection
import com.zoeyjuan.movetracker.R
import com.zoeyjuan.movetracker.database.UserLocation
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class LocationTrackingService: Service(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        val ACTION_LOCATION_BROADCAST = "ActionLocationBroadcast"
        val MSG_LOCATION = "Location"
        val MSG_LAT = "Lat"
        val MSG_LON = "Lon"
    }

    private val TAG: String = LocationTrackingService::class.toString()

    private var userLocationRepository = Injection.provideUserLocationRepository(this)
    private val locations: MutableList<UserLocation> = mutableListOf()
    private val locationBroadcastReceiver = LocationBroadcastReceiver(locations, Calendar.getInstance().timeInMillis)
    private lateinit var  mLocationClient: GoogleApiClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val mLocationRequest = LocationRequest().apply {
        interval = 1L
        fastestInterval = 1L
        smallestDisplacement = 1.0F
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations){
                broadcastLocation(location)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        stopLocationUpdates()
        return false
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service Started.")

        startForeground(1, showNotification())

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(locationBroadcastReceiver, IntentFilter(ACTION_LOCATION_BROADCAST))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Received start id $startId: $intent")
        connectToGoogleApiClient()
        return START_STICKY // run until explicitly stopped.
    }

    override fun onDestroy() {
        if (mLocationClient.isConnected)
            mLocationClient.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver)

        Log.i(TAG, "Service Stopped.")
        super.onDestroy()
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "== Error On onConnected() Permission not granted")
            //Permission not granted by user so cancel the further execution.
            return
        }
        startLocationUpdates()

        Log.d(TAG, "Connected to Google API")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "Google API onConnectionSuspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "Google API onConnectionFailed")
    }

    private fun connectToGoogleApiClient() {
            mLocationClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            mLocationClient.connect()
    }

    private fun showNotification(): Notification {
        val notification = NotificationCompat.Builder(this,"LocationServiceId")
                .setSmallIcon(R.drawable.navigation_empty_icon)
                .setContentTitle("Notification from MoveTracker")
                .setContentText("It's tracking you!")
                .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("LocationServiceId","LocationService",
                    NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(channel)
        }
        return notification
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                null /* Looper */)
    }

    private fun stopLocationUpdates() {
        writeToDb(locations)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun broadcastLocation(location: Location) {
        Log.d(TAG, "Google API onLocationChanged broadcastLocation")
        val intent = Intent(ACTION_LOCATION_BROADCAST)
        intent.putExtra(MSG_LOCATION, location)
        intent.putExtra(MSG_LAT, location.latitude)
        intent.putExtra(MSG_LON, location.longitude)
        LocalBroadcastManager.getInstance(this@LocationTrackingService).sendBroadcast(intent)
    }


    private fun writeToDb(locations: MutableList<UserLocation>) {
        Completable.fromAction {
            userLocationRepository.saveLocations(locations)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.i(TAG, "track writeToDb success")
                }, { error ->
                    Log.i(TAG, "track writeToDb error")
                })
    }
}