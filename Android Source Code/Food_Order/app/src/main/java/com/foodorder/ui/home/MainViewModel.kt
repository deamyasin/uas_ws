package com.foodorder.ui.home

import Config.BaseURL
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.onesignal.OneSignal
import utils.SessionManagement

/**
 * Created on 05-05-2020.
 */
class MainViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun registerPlayerId(params: Map<String, String>): LiveData<CommonResponse?> {
        return projectRepository.registerPlayerId(params)
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

            OneSignal.sendTag("FoodOrder", "1")

            if (isRegister) {
                val params = HashMap<String, String>()
                params["user_id"] =
                    SessionManagement.UserData.getSession(context, BaseURL.KEY_ID)
                params["player_id"] = userId
                params["device"] = "android"

                registerPlayerId(params)
            }
            Log.i("TEST", text)
        }

    }

}