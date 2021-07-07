package com.fooddeliveryboy.ui.tracking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.OrderModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dialogs.LoaderDialog
import kotlinx.android.synthetic.main.activity_tracking.*
import org.json.JSONObject
import utils.ContextWrapper
import utils.DrawRoute
import utils.LanguagePrefs
import utils.TrackerService
import java.util.*
import kotlin.collections.HashMap


class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    public companion object {
        private val TAG = TrackingActivity::class.java.simpleName
        private const val PERMISSIONS_REQUEST = 1
        public var ORDER_ID: String = ""
    }

    private var isTrackingStarted = false
    private val mMarkers: HashMap<String, Marker> = HashMap()
    private var lastLatLng: LatLng? = null
    private var currentMarker: Marker? = null

    private lateinit var mMap: GoogleMap

    lateinit var orderModel: OrderModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        LanguagePrefs(this)
        setContentView(R.layout.activity_tracking)

        orderModel = intent.getSerializableExtra("orderData") as OrderModel

        ORDER_ID = orderModel.order_id!!

        tv_track_order_address.text = orderModel.address_line1
        tv_track_order_id.text = orderModel.order_id
        tv_track_order_item.text = orderModel.total_qty
        tv_track_order_total_price.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(
                orderModel.net_amount,
                true
            ), true
        )

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (CommonActivity.isMyServiceRunning(this, TrackerService::class.java)) {
            subscribeToUpdates()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()
        //mMap.setMaxZoomPreference(16F)

        if (!orderModel.latitude.isNullOrEmpty() && !orderModel.longitude.isNullOrEmpty()) {
            val latLngStart =
                LatLng(orderModel.latitude!!.toDouble(), orderModel.longitude!!.toDouble())
            mMap.addMarker(
                MarkerOptions().position(latLngStart)
                    .title(resources.getString(R.string.order_location))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user))
            ).showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngStart, 21F))
            if (orderModel.status == "1") {
                requestLocationUpdates()
            }
        }

    }

    private fun drawRoute(latLngStart: LatLng, latLngDestination: LatLng) {
        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        val drawRoute = DrawRoute(this)
        val routeUrl = drawRoute.getDirectionsUrl(latLngStart, latLngDestination)
        drawRoute.DownloadTask(DrawRoute.OnRouteListener { response: String, pathList: List<HashMap<String, String>>, lineOptions ->
            //Log.d(TAG, "RouteLatLngListDATA::$pathList")

            loaderDialog.dismiss()

            if (lineOptions != null) {
                mMap.addPolyline(lineOptions)
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngStart, 21F))

            distanceCheck(response)
        }).execute(routeUrl)
    }

    private fun distanceCheck(response: String) {
        var totalDistance = ""
        var totalDuration = ""

        val jsonObject = JSONObject(response)
        if (jsonObject.has("routes")) {
            val jsonArrayRoutes = jsonObject.getJSONArray("routes")
            for (route in 0 until jsonArrayRoutes.length()) {
                if (jsonArrayRoutes.getJSONObject(route).has("legs")) {
                    val jsonArrayLegs =
                        jsonArrayRoutes.getJSONObject(route).getJSONArray("legs")
                    for (legs in 0 until jsonArrayLegs.length()) {
                        val jsonObjectLegs = jsonArrayLegs.getJSONObject(legs)
                        if (totalDistance.isEmpty()) {
                            val jsonObjectDistance = jsonObjectLegs.getJSONObject("distance")
                            val jsonObjectDuration = jsonObjectLegs.getJSONObject("duration")

                            totalDistance = jsonObjectDistance.getString("text")
                            totalDuration = jsonObjectDuration.getString("text")

                            iv_track_order_km.text = totalDistance
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    mMap.isMyLocationEnabled = true
                    displayLocationSettingsRequest(this@TrackingActivity)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //onPermissionDenied
                }
            }).check()
    }

    private fun startTrackerService() {
        startService(Intent(this, TrackerService::class.java))
    }

    private val REQUEST_CHECK_SETTINGS = 0x1

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.i(
                        TAG, "All location settings are satisfied."
                    )
                    if (!isTrackingStarted && !CommonActivity.isMyServiceRunning(
                            this@TrackingActivity,
                            TrackerService::class.java
                        )
                    ) {
                        Log.d(TAG, "Service Started")
                        if (orderModel.status == "2") {
                            startTrackerService()
                            subscribeToUpdates()
                        }
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@TrackingActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 500
        request.fastestInterval = 500
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client =
            LocationServices.getFusedLocationProviderClient(this)
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        Log.d(TAG, "location update $location")
                        val latLngCurrent = LatLng(location.latitude, location.longitude)
                        if (currentMarker == null) {
                            currentMarker = mMap.addMarker(
                                MarkerOptions().position(latLngCurrent)
                                    .title(resources.getString(R.string.your_location))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_driver))
                            )
                        } else {
                            currentMarker?.position = latLngCurrent
                        }
                    }
                }
            }, null)
        }
    }

    private fun subscribeToUpdates() {
        val path = "locations/$ORDER_ID"
        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(path)
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                setMarker(dataSnapshot)
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                setMarker(dataSnapshot)
            }

            override fun onChildMoved(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                //onChildMoved
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                //onChildRemoved
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(
                    TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }

    val value = HashMap<String?, Any?>()

    private fun setMarker(dataSnapshot: DataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once

        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        val key = dataSnapshot.key
        value[key] = dataSnapshot.value
        if (value["latitude"] != null && value["longitude"] != null) {
            val lat = value["latitude"].toString().toDouble()
            val lng = value["longitude"].toString().toDouble()
            val location = LatLng(lat, lng)
            if (!mMarkers.containsKey("123")) {
                mMarkers["123"] = mMap.addMarker(
                    MarkerOptions().title(key).position(location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_driver))
                )
            } else {
                mMarkers["123"]!!.position = location
            }
            val builder: LatLngBounds.Builder = LatLngBounds.Builder()
            for (marker in mMarkers.values) {
                builder.include(marker.position)
            }
            if (lastLatLng == null) {
                lastLatLng = location
                if (!orderModel.latitude.isNullOrEmpty() && !orderModel.longitude.isNullOrEmpty()) {
                    val destinationLatLng =
                        LatLng(orderModel.latitude!!.toDouble(), orderModel.longitude!!.toDouble())
                    drawRoute(lastLatLng!!, destinationLatLng)
                }
            }
            //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
