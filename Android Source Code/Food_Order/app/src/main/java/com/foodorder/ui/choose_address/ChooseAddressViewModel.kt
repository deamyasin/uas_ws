package com.foodorder.ui.choose_address

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created on 07-05-2020.
 */
class ChooseAddressViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = "ChooseAddressViewModel"
        private val REQUEST_CHECK_SETTINGS = 0x1
    }

    val projectRepository = ProjectRepository()

    val permissionListenerLiveData = MutableLiveData<Boolean>()
    val locationSettingUpdateLiveData = MutableLiveData<LocationSettingsResult>()
    val locationUnableLiveData = MutableLiveData<Boolean>()
    val locationUpdateLiveData = MutableLiveData<Location>()

    fun makeAddAddress(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.addAddress(params)
    }

    fun makeEditAddress(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.updateAddress(params)
    }

    fun checkLocationPermission(context: Context) {
        Dexter.withActivity(context as Activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    permissionListenerLiveData.value = true
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    permissionListenerLiveData.value = false
                }
            }).check()
    }

    fun checkLocationSettingsRequest(context: Activity) {
        Log.e(TAG, "sdfsfsdfsdf")
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 100
        locationRequest.fastestInterval = 100
        locationRequest.numUpdates = 1
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { results: LocationSettingsResult ->
            val status = results.status
            locationSettingUpdateLiveData.value = results
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    locationUnableLiveData.value = true
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    locationUnableLiveData.value = false
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            context,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    locationUnableLiveData.value = false
                }
            }
        }
    }

    var client: FusedLocationProviderClient? = null

    fun requestLocationUpdates(context: Context) {
        val request = LocationRequest()
        request.interval = 1000
        request.fastestInterval = 1000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        client = LocationServices.getFusedLocationProviderClient(context as Activity)
        val permission = ContextCompat.checkSelfPermission(
            context as Activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client?.requestLocationUpdates(request, OnLocationCallBack(), null)
        }
    }

    fun removeLocationUpdate() {
        val voidTask: Task<Void>? = client?.removeLocationUpdates(OnLocationCallBack())
        voidTask?.addOnCompleteListener { task: Task<Void> ->
            Log.d(TAG, "addOnCompleteListener::${task.isComplete}")
        }
        voidTask?.addOnFailureListener { exception ->
            Log.e(TAG, "addOnFailureListener::${exception.toString()}")
        }

    }

    inner class OnLocationCallBack() : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                Log.d(TAG, "location update $location")
                locationUpdateLiveData.value = location
            }
        }
    }

    fun getLocationAddress(context: Context, latLng: LatLng): LiveData<Address?> {
        val addressLiveData = MutableLiveData<Address?>()
        viewModelScope.launch {
            try {
                val geoCoder = Geocoder(context, Locale.getDefault())
                val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (address.size > 0) {
                    val addressLine = address[0].getAddressLine(0)

                    Log.d(TAG, "getLocationAddress: $addressLine")
                    addressLiveData.value = address[0]
                } else {
                    addressLiveData.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addressLiveData.value = null
            }
        }
        return addressLiveData
    }

}