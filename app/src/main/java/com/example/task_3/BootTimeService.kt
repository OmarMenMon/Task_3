package com.example.task_3

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log

// Service that provides statistics about the system boot time
class BootTimeService : Service() {

    // Implementation of the AIDL interface
    // This object handles the client calls to the service methods defined in AIDL
    private val binder = object : IBootTimeService.Stub() {
        override fun getBootTime(): Long {
            // Return the system boot time in milliseconds since the system was last booted
            return SystemClock.elapsedRealtime()
        }
    }

    // Called when the service is first created
    override fun onCreate() {
        super.onCreate()
        Log.d("BootTimeService", "Service created")
    }

    // Called when the service is bound to a client
    // Returns the binder object to allow the client to call service methods
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}