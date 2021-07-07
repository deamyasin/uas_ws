package utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.ui.tracking.TrackingActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Created on 04-04-2020.
 */
class TrackerService : Service() {

    var client: FusedLocationProviderClient? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        SessionManagement.PermanentData.setSession(
            this,
            "isServiceStart",
            true
        )
        buildNotification()
        requestLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        removeLocationUpdate()
        SessionManagement.PermanentData.setSession(this, "isServiceStart", false)
    }

    private fun buildNotification() {
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(this, TrackerService::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = "556235"

        // Create the persistent notification
        val builder =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(resources.getString(R.string.tracking_on))
                .setContentText(resources.getString(R.string.this_notification_disappear_after_all_item_delivered))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_marker)
                .setChannelId(channelId)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = resources.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        startForeground(1, builder.build())
    }

    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 500
        request.fastestInterval = 500
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        client = LocationServices.getFusedLocationProviderClient(this)
        val path = "locations/${TrackingActivity.ORDER_ID}"
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client?.requestLocationUpdates(request, OnLocationCallBack(path), null)
        }
    }

    private fun removeLocationUpdate() {
        val voidTask: Task<Void>? = client?.removeLocationUpdates(OnLocationCallBack(""))
        voidTask?.addOnCompleteListener { task: Task<Void> ->
            Log.d(TAG, "addOnCompleteListener::${task.isComplete}")
        }
        voidTask?.addOnFailureListener { exception ->
            Log.e(TAG, "addOnFailureListener::${exception.toString()}")
        }

    }

    inner class OnLocationCallBack(val path: String) : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (SessionManagement.PermanentData.getSessionBoolean(
                    this@TrackerService,
                    "isServiceStart"
                )
            ) {
                val ref = FirebaseDatabase.getInstance().getReference("locations")
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d(TAG, "location update $location")

                    val hashMap = HashMap<String, Any>()
                    val orderids = SessionManagement.PermanentData.getSession(
                        this@TrackerService,
                        "orderIds"
                    )
                    if (orderids.isNotEmpty()) {
                        val stroHasmap = CommonActivity.convertToHashMap(orderids)
                        for (has in stroHasmap) {
                            hashMap[has.key] = location
                        }
                    }

                    if (hashMap.size > 0) {
                        //ref.setValue(location)
                        ref.updateChildren(hashMap)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = TrackerService::class.java.simpleName
    }

}