package com.foodorder.ui.order_detail

import Dialogs.LoaderDialog
import android.os.Bundle
import android.util.Log
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.ui.order_detail.adapter.MyInfoWindowAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import org.json.JSONObject
import utils.DrawRoute

class OrderTrackMapActivity : CommonActivity(), OnMapReadyCallback {

    companion object {
        val TAG = OrderTrackMapActivity::class.java.simpleName
    }

    private val mMarkers: HashMap<String, Marker> = HashMap()
    private var lastLatLng: LatLng? = null
    private lateinit var mMap: GoogleMap

    lateinit var orderModel: OrderModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_track_map)
        setHeaderTitle(resources.getString(R.string.track_order))

        orderModel = intent.getSerializableExtra("orderData") as OrderModel

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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

        mMap.setInfoWindowAdapter(MyInfoWindowAdapter(this@OrderTrackMapActivity))

        if (!orderModel.latitude.isNullOrEmpty() && !orderModel.longitude.isNullOrEmpty()) {
            val latLngStart =
                LatLng(orderModel.latitude!!.toDouble(), orderModel.longitude!!.toDouble())
            mMap.addMarker(
                MarkerOptions().position(latLngStart)
                    .title(resources.getString(R.string.delivery_location))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_user))
            ).showInfoWindow()
        }

        subscribeToUpdates()
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngDestination, 21F))

            routeDistance(response)

        }).execute(routeUrl)
    }

    private fun routeDistance(response: String) {
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
                        val jsonObjectDistance = jsonObjectLegs.getJSONObject("distance")
                        val jsonObjectDuration = jsonObjectLegs.getJSONObject("duration")

                        totalDistance = jsonObjectDistance.getString("text")
                        totalDuration = jsonObjectDuration.getString("text")
                        break
                    }
                }
            }
        }
    }

    private fun subscribeToUpdates() {
        val path = "locations/${orderModel.order_id}"
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
        value[dataSnapshot.key] = dataSnapshot.value
        if (value["latitude"] != null && value["longitude"] != null) {
            val lat = value["latitude"].toString().toDouble()
            val lng = value["longitude"].toString().toDouble()
            val location = LatLng(lat, lng)

            updateDeliveryMarker(location)
            //mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
        }
    }

    private fun updateDeliveryMarker(location: LatLng) {
        if (!mMarkers.containsKey("123")) {
            mMarkers["123"] = mMap.addMarker(
                MarkerOptions().title(orderModel.boy_name)
                    .snippet(orderModel.boy_phone)
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_driver))
            )
            if (value["bearing"] != null)
                mMarkers["123"]!!.rotation = value["bearing"].toString().toFloat()
        } else {
            mMarkers["123"]!!.position = location
            if (value["bearing"] != null)
                mMarkers["123"]!!.rotation = value["bearing"].toString().toFloat()
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
                drawRoute(destinationLatLng, lastLatLng!!)
            }
        }
    }

}