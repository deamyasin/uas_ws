package com.foodorder.ui.choose_address

import Config.BaseURL
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.AddressModel
import com.foodorder.response.CommonResponse
import kotlinx.android.synthetic.main.activity_choose_address.*
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.LocationFinder
import utils.SessionManagement
import java.io.Serializable

class ChooseAddressActivity : CommonActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener {

    companion object {
        val TAG = ChooseAddressActivity::class.java.simpleName
        private val REQUEST_CHECK_SETTINGS = 0x1
    }

    private var addressModel: AddressModel? = null

    private lateinit var mMap: GoogleMap

    lateinit var chooseAddressViewModel: ChooseAddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseAddressViewModel = ViewModelProvider(this)[ChooseAddressViewModel::class.java]
        setContentView(R.layout.activity_choose_address)
        setHeaderTitle(resources.getString(R.string.select_delivery_address))

        if (intent.hasExtra("addressData")) {
            addressModel = intent.getSerializableExtra("addressData") as AddressModel
        }

        chooseAddressViewModel.permissionListenerLiveData.observe(this, Observer { isGranted ->
            if (isGranted) {
                chooseAddressViewModel.checkLocationSettingsRequest(this)
            }
        })

        chooseAddressViewModel.locationUnableLiveData.observe(this, Observer { isGPSEnable ->
            if (isGPSEnable) {
                chooseAddressViewModel.requestLocationUpdates(this)
            }
        })

        var lastLocation: Location? = null

        chooseAddressViewModel.locationUpdateLiveData.observe(this, Observer { location: Location ->
            if (lastLocation == null && addressModel == null) {
                lastLocation = location
                val latLng = LatLng(location.latitude, location.longitude)
                //mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
                mMap.isMyLocationEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_choose_address) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_choose_address_deliver_here.setOnClickListener {
            val latLng = mMap.cameraPosition.target
            chooseAddressViewModel.getLocationAddress(this, latLng)
                .observe(this, Observer { address: Address? ->
                    if (address != null)
                        checkLocationAddress(address, latLng)
                })
        }
    }

    private fun checkLocationAddress(address: Address, latLng: LatLng) {
        val addressLine = address.getAddressLine(0)
        val featureName = address.featureName
        val thoroughfare = address.thoroughfare
        val cityName = address.locality
        val countryName = address.countryName
        val postalCode = address.postalCode

        if (!postalCode.isNullOrEmpty()) {
            val params = HashMap<String, String>()
            params["user_id"] =
                SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
            params["postal_code"] = postalCode
            params["address_line1"] = addressLine
            params["address_line2"] = ""
            params["city"] = if (cityName.isNullOrEmpty()) "" else cityName
            params["latitude"] = latLng.latitude.toString()
            params["longitude"] = latLng.longitude.toString()

            if (ConnectivityReceiver.isConnected) {
                if (addressModel != null) {
                    params["user_address_id"] = addressModel?.user_address_id!!
                    makeEditAddress(params)
                } else {
                    makeAddAddress(params)
                }
            } else {
                ConnectivityReceiver.showSnackbar(this)
            }
        }
    }

    private fun makeAddAddress(params: HashMap<String, String>) {

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        chooseAddressViewModel.makeAddAddress(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        CommonActivity.showToast(this, response.message!!)
                        val gson = Gson()
                        val type = object : TypeToken<AddressModel>() {}.type
                        val addressModel = gson.fromJson<AddressModel>(
                            response.data?.toString(),
                            type
                        )
                        Intent().apply {
                            setResult(Activity.RESULT_OK, this)
                            putExtra("addressData", addressModel as Serializable)
                            finish()
                        }
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeEditAddress(params: HashMap<String, String>) {

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        chooseAddressViewModel.makeEditAddress(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        CommonActivity.showToast(this, response.message!!)
                        val gson = Gson()
                        val type = object : TypeToken<AddressModel>() {}.type
                        val addressModel = gson.fromJson<AddressModel>(
                            response.data?.toString(),
                            type
                        )
                        Intent().apply {
                            setResult(Activity.RESULT_OK, this)
                            putExtra("itemPosition", intent.getIntExtra("itemPosition", 0))
                            putExtra("addressData", addressModel as Serializable)
                            finish()
                        }
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
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

        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveCanceledListener(this)

        // Add a marker in Sydney and move the camera
        if (addressModel != null) {
            val latLng =
                LatLng(addressModel?.latitude!!.toDouble(), addressModel?.longitude!!.toDouble())
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
        } /*else {
            val locationFinder = LocationFinder(this)
            if (locationFinder.canGetLocation()) {
                val latLng = LatLng(locationFinder.latitude, locationFinder.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
            }*//* else {
                locationFinder.showSettingsAlert()
            }*//*
        }*/
        chooseAddressViewModel.checkLocationPermission(this)
    }

    override fun onCameraIdle() {
        showProgress(true)
        val latLng = mMap.cameraPosition.target
        Log.e(TAG, "chooseLatLng: ${latLng.latitude}, ${latLng.longitude}")
        showLocation(false)

        chooseAddressViewModel.getLocationAddress(this, latLng)
            .observe(this, Observer { address: Address? ->
                if (address != null) {
                    if (!address.postalCode.isNullOrEmpty()) {
                        tv_choose_address_location.text = address.getAddressLine(0)

                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            showLocation(true)
                        }, 1000)
                    } else {
                        showLocation(false)
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            pb_choose_address.visibility = View.GONE
                            btn_choose_address_deliver_here.text =
                                getString(R.string.unable_to_detect_location)
                        }, 500)
                    }
                } else {
                    showLocation(false)
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        pb_choose_address.visibility = View.GONE
                        btn_choose_address_deliver_here.text =
                            getString(R.string.unable_to_detect_location)
                    }, 500)

                }
            })
    }

    override fun onCameraMoveStarted(p0: Int) {
        //onCameraMoveStarted
    }

    override fun onCameraMove() {
        showProgress(false)
        showLocation(false)
    }

    override fun onCameraMoveCanceled() {
        showProgress(true)
    }

    private fun showProgress(show: Boolean) {
        showLocation(false)

        if (show) {
            mMap.setPadding(0, 0, 0, 135)

            val animate = TranslateAnimation(0f, 0F, ll_choose_address_delivery.width.toFloat(), 0f)
            animate.duration = 400
            animate.fillAfter = true
            ll_choose_address_delivery.startAnimation(animate)
            ll_choose_address_delivery.visibility = View.VISIBLE
            pb_choose_address.visibility = View.VISIBLE
            btn_choose_address_deliver_here.text = getString(R.string.detecting_location)
        } else {
            mMap.setPadding(0, 0, 0, 10)
            ll_choose_address_delivery.visibility = View.GONE
        }

    }

    private fun showLocation(show: Boolean) {
        if (show) {
            if (ll_choose_address_location.visibility == View.GONE) {
                Log.e(TAG, "show")
                pb_choose_address.visibility = View.GONE
                btn_choose_address_deliver_here.text = resources.getString(R.string.deliver_here)

                //ll_choose_address_location.setBackgroundColor(resources.getColor(R.color.colorWhite))

                mMap.setPadding(0, 0, 0, 300)
                val animate =
                    TranslateAnimation(0f, 0F, ll_choose_address_location.width.toFloat(), 0f)
                animate.duration = 400
                animate.fillAfter = true
                ll_choose_address_location.startAnimation(animate)
                ll_choose_address_location.visibility = View.VISIBLE
                ll_choose_address_address.visibility = View.VISIBLE
                //tv_choose_address_lable.visibility = View.VISIBLE
            }
        } else {
            if (ll_choose_address_location.visibility == View.VISIBLE) {
                Log.e(TAG, "hide")
                mMap.setPadding(0, 0, 0, 135)
                ll_choose_address_location.visibility = View.GONE
                ll_choose_address_address.visibility = View.GONE
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                chooseAddressViewModel.requestLocationUpdates(this)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                chooseAddressViewModel.checkLocationPermission(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chooseAddressViewModel.removeLocationUpdate()
    }

}
