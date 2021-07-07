package utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fooddeliveryboy.R
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class NotificationExtender : NotificationExtenderService() {

    override fun onNotificationProcessing(receivedResult: OSNotificationReceivedResult): Boolean {
        Log.d("OneSignalExample", "Notification data: " + receivedResult.payload.additionalData)
        Log.d("OneSignalExample", "Notification title: " + receivedResult.payload.title)
        Log.d("OneSignalExample", "Notification body: " + receivedResult.payload.body)
        Log.d("OneSignalExample", "Notification big picture: " + receivedResult.payload.bigPicture)
        Log.d("OneSignalExample", "Notification small icon: " + receivedResult.payload.smallIcon)
        Log.d("OneSignalExample", "Notification large icon: " + receivedResult.payload.largeIcon)

        val isBigStyle = (!receivedResult.payload.bigPicture.isNullOrEmpty())

        val overrideSettings = OverrideSettings()
        overrideSettings.extender = if (isBigStyle) {
            NotificationCompat.Extender { builder ->
                val bm = BitmapFactory.decodeResource(resources, R.drawable.ic_logo)
                builder.setLargeIcon(bm)
                builder.setSmallIcon(R.drawable.ic_stat_notification)
                builder.color = ContextCompat.getColor(this, R.color.colorPrimary)
                builder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(
                        getBitmapFromURL(
                            receivedResult.payload.bigPicture
                        )
                    )
                )
            }
        } else {
            NotificationCompat.Extender { builder ->
                val bm = BitmapFactory.decodeResource(resources, R.drawable.ic_logo)
                builder.setLargeIcon(bm)
                builder.setSmallIcon(R.drawable.ic_stat_notification)
                builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }

        val displayedResult = displayNotification(overrideSettings)
        Log.d(
            "OneSignalExample",
            "Notification displayed with id3: " + displayedResult.androidNotificationId
        )

        val counter = SessionManagement.CommonData.getSessionInt(this, "notificationCounter")
        SessionManagement.CommonData.setSession(this, "notificationCounter", counter + 1)

        val updates = Intent("NotificationUpdate")
        updates.putExtra("type", "updateNotification")
        sendBroadcast(updates)

        return true
    }

    fun getBitmapFromURL(strURL: String): Bitmap? {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}