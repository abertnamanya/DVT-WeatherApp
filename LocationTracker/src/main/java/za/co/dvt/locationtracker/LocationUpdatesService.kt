package za.co.dvt.locationtracker

import android.app.*
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.gms.location.*


class LocationUpdatesService : Service() {
    private val mBinder = LocalBinder()
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    private var mChangingConfiguration = false
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mLocationCallback: LocationCallback? = null

    private var mServiceHandler: Handler? = null

    private var currentLocation: Location? = null
    private val TAG = "LocationUpdatesService"

    private var listPlaces: ArrayList<Location>? = null

    override fun onCreate() {
        super.onCreate()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                location?.let { location ->
                    location.lastLocation?.let { onNewLocation(it) }
                }
            }
        }

        createLocationRequest()
        getLastLocation()

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mServiceHandler?.removeCallbacksAndMessages(null)
    }

    fun requestLocationUpdates() {
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest!!,
                mLocationCallback!!,
                Looper.myLooper()
            )
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }

    }

    private fun removeLocationUpdates() {
        try {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback!!)
            stopSelf()
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }


    private fun getLastLocation() {
        try {
            mFusedLocationClient?.lastLocation
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        currentLocation = task.result
                    }
                }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }
    }

    fun onNewLocation(location: Location) {
        currentLocation = location
        if (!isAppIsInBackground(applicationContext)) {
            val intent = Intent("LocationUpdates")
            intent.putExtra("current_location", currentLocation!!.provider)
            intent.putExtra(
                "current_location_latitude",
                currentLocation!!.latitude.toString()
            )
            intent.putExtra(
                "current_location_longitude",
                currentLocation!!.longitude.toString()
            )
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
    }

    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService
            get() = this@LocationUpdatesService
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true

        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            am?.let {
                val runningProcesses = it.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isInBackground
    }

}