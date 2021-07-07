package com.foodorder

import Config.BaseURL
import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.droidnet.DroidNet
import com.foodorder.ui.splash.SplashActivity
import com.onesignal.OSNotification
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import org.json.JSONObject
import utils.LanguagePrefs

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        DroidNet.init(this)

        // OneSignal Initialization
        OneSignal.startInit(this)
            .setNotificationReceivedHandler(ExampleNotificationReceivedHandler())
            .setNotificationOpenedHandler(ExampleNotificationOpenedHandler())
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()

        LanguagePrefs(this)
        BaseURL.HEADER_LANG = if (LanguagePrefs.getLang(this).equals("nl")) {
            "dutch"
        } else if (LanguagePrefs.getLang(this).equals("ar")) {
            "arabic"
        } else {
            "english"
        }

    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
            private set
    }

    @Synchronized
    fun getInstance(): AppController? {
        return instance
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        if (LanguagePrefs.getLang(this) != null) {
            LanguagePrefs(this)
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    inner class ExampleNotificationReceivedHandler :
        OneSignal.NotificationReceivedHandler {
        override fun notificationReceived(notification: OSNotification) {
            val data = notification.payload.additionalData
            val notificationID = notification.payload.notificationID
            val title = notification.payload.title
            val body = notification.payload.body
            val smallIcon = notification.payload.smallIcon
            val largeIcon = notification.payload.largeIcon
            val bigPicture = notification.payload.bigPicture
            val smallIconAccentColor = notification.payload.smallIconAccentColor
            val sound = notification.payload.sound
            val ledColor = notification.payload.ledColor
            val lockScreenVisibility = notification.payload.lockScreenVisibility
            val groupKey = notification.payload.groupKey
            val groupMessage = notification.payload.groupMessage
            val fromProjectNumber = notification.payload.fromProjectNumber
            val rawPayload = notification.payload.rawPayload
            val customKey: String?
            Log.i("OneSignalExample", "NotificationID received: $notificationID")
            if (data != null) {
                Log.i("OneSignalExample", "" + data.toString())
                customKey = data.optString("customkey", null)
                if (customKey != null) Log.i(
                    "OneSignalExample",
                    "customkey set with value: $customKey"
                )
            }
        }
    }

    inner class ExampleNotificationOpenedHandler : OneSignal.NotificationOpenedHandler {
        override fun notificationOpened(result: OSNotificationOpenResult) {
            val notificationID = result.notification.payload.notificationID
            val data = result.notification.payload.additionalData
            val customKey: String?
            var openURL: String? = null
            val activityToLaunch: Any = SplashActivity::class.java
            if (data != null) {
                Log.i("OneSignalExample", "data::" + data.toString())
                customKey = data.optString("customkey", null)
                openURL = data.optString("openURL", null)
                if (customKey != null) Log.i(
                    "OneSignalExample",
                    "customkey set with value: $customKey"
                )
                if (openURL != null) Log.i(
                    "OneSignalExample",
                    "openURL to webview with URL value: $openURL"
                )
            }

            val intent =
                Intent(instance, activityToLaunch as Class<*>)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("data", data.toString())
            Log.i("OneSignalExample", "openURL =$openURL")
            startActivity(intent)
        }
    }

}