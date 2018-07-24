package com.zoeyjuan.movetracker.ui

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.common.GooglePlayServicesUtil

import kotlinx.android.synthetic.main.activity_main.*
import com.zoeyjuan.movetracker.service.LocationTrackingService
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.google.android.gms.common.ConnectionResult
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.zoeyjuan.movetracker.R
import com.zoeyjuan.movetracker.service.LocationServiceConnectionManager
import com.zoeyjuan.movetracker.ui.util.StaticMapInfoGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.toString()

    // TODO: Replaced by Dagger Injection for testing easily
    private val uploadCheckDialog = UploadCheckDialog()
    private val locationServiceConnectionManager
            = LocationServiceConnectionManager(this, LocationTrackingService::class.java)

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UserLocationViewModel
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        inject()
        setupRecycleViewAdapter()
        observeViewModel()
        setupFloatingButton()
    }

    override fun onResume() {
        super.onResume()
        checkGooglePlayServices(this)
        checkPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        stopRecordLocation(false)
        super.onDestroy()
    }

    fun upload() {
        viewModel.updateUserLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "Upload finished", Toast.LENGTH_LONG).show()
                },
                        { error -> Log.e(TAG, "Upload failed", error) })
    }

    private fun startRecordLocation() {
        locationServiceConnectionManager.startService()
        locationServiceConnectionManager.bindToService()
    }

    private fun stopRecordLocation(isShowDialog: Boolean) {
        locationServiceConnectionManager.unbindFromService()
        locationServiceConnectionManager.stopService()
        if (isShowDialog) {
            uploadCheckDialog.show(this)
        }
    }

    private fun inject() {
        viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserLocationViewModel::class.java)
        trackAdapter = TrackAdapter(layoutInflater, StaticMapInfoGenerator(), mutableListOf(), Picasso.get())
        linearLayoutManager = LinearLayoutManager(this)
    }

    private fun setupRecycleViewAdapter() {
        recyclerView.adapter = trackAdapter
        recyclerView.layoutManager = (linearLayoutManager)
    }

    private fun observeViewModel() {
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.getAllLocation().observe(this, Observer  {
            (recyclerView.adapter as TrackAdapter).updateList(list = it?.toMutableList()?: mutableListOf())
            recyclerView.adapter.notifyDataSetChanged()
        })
    }

    private fun setupFloatingButton() {
        fab.setOnClickListener {
            when (locationServiceConnectionManager.isStarted()) {
                true -> {
                    stopRecordLocation(true)
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play))
                }
                false -> {
                    startRecordLocation()
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.stop))
                }
            }
        }
    }

    // TODO: Replace by Injection
    private fun checkPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET)
                .withListener(DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(this)
                        .withTitle("Camera & audio permission")
                        .withMessage("Both camera and audio permission are needed to take pictures of your cat")
                        .withButtonText("ok")
                        .withIcon(R.mipmap.ic_launcher)
                        .build())
                .check()
    }

    // TODO: Replace by Injection, remove deprecated function
    private fun checkGooglePlayServices(activity: Activity): Boolean {
        val googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
        when (googlePlayServicesCheck) {
            ConnectionResult.SUCCESS -> return true
            ConnectionResult.SERVICE_DISABLED, ConnectionResult.SERVICE_INVALID,
            ConnectionResult.SERVICE_MISSING, ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                val dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0)
                dialog.setOnCancelListener { activity.finish() }
                dialog.show()
            }
        }
        return false
    }
}
