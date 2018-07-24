package com.zoeyjuan.movetracker.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder

class LocationServiceConnectionManager(private val context: Context,
                                       private val service: Class<out Service>) : ServiceConnection {

    private var isBinded = false
    private var isBounded = false

    fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, service))
        } else {
            context.startService(Intent(context, service))
        }
    }

    fun stopService() {
        context.stopService(Intent(context, service))
    }

    fun bindToService() {
        if (!isBinded) {
            isBinded = true
            isBounded = context.bindService(Intent(context, service), this, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindFromService() {
        if (isBinded) {
            context.unbindService(this)
            isBinded = false
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isBounded = false
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {}

    fun isStarted(): Boolean = isBounded && isBinded
}