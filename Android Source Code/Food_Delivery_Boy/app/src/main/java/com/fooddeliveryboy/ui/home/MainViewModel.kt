package com.fooddeliveryboy.ui.home

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fooddeliveryboy.BuildConfig
import com.fooddeliveryboy.repository.ProjectRepository
import com.fooddeliveryboy.response.CommonResponse
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import com.onesignal.OneSignal
import utils.SessionManagement

/**
 * Created on 06-04-2020.
 */
class MainViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val locationSettingUpdateLiveData = MutableLiveData<LocationSettingsResult>()
    val locationUnableLiveData = MutableLiveData<Boolean>()

    fun makeRegisterFCM(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.registerFCM(params)
    }

    fun makeAddAvailable(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.setAvailable(params)
    }

    fun makeFirebaseSubscribe(context: Context, isRegister: Boolean) {
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("Food_Delivery_Boy")
            .addOnCompleteListener { task ->
                var msg = "success"
                if (!task.isSuccessful) {
                    msg = "failed"
                }
                Log.d(context.toString(), msg)
            }
        // [END subscribe_topics]

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(context as Activity,
            OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                val token = instanceIdResult.token
                Log.e(context.toString(), token)
                if (isRegister) {
                    val params = HashMap<String, String>()
                    params["delivery_boy_id"] =
                        SessionManagement.UserData.getSession(context, BaseURL.KEY_ID)
                    params["token"] = token
                    params["device"] = BuildConfig.HEADER_DEVICE

                    makeRegisterFCM(params)
                }
            })*/

        OneSignal.idsAvailable { userId, registrationId ->
            var text = "OneSignal UserID:\n$userId\n\n"

            text += if (registrationId != null)
                "Google Registration Id:\n$registrationId"
            else
                "Google Registration Id:\nCould not subscribe for push"

            OneSignal.sendTag("FoodDeliveryBoy", "1")

            val params = HashMap<String, String>()
            params["delivery_boy_id"] =
                SessionManagement.UserData.getSession(context, BaseURL.KEY_ID)
            params["player_id"] = userId
            params["device"] = "android"

            makeRegisterFCM(params)
            Log.i("TEST", text)
        }

    }

    fun checkLocationSettingsRequest(context: Context) {
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
        result.setResultCallback { result: LocationSettingsResult ->
            val status = result.status
            locationSettingUpdateLiveData.value = result
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    locationUnableLiveData.value = true
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    locationUnableLiveData.value = false
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    locationUnableLiveData.value = false
                }
            }
        }
    }

}