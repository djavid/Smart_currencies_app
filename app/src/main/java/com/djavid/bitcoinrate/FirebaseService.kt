package com.djavid.bitcoinrate

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.view.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            showNotification(remoteMessage)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)

            showNotification(remoteMessage)
        }
    }

    private fun showNotification(remoteMessage: RemoteMessage) {

        //TODO show another notifications

        val context = applicationContext

        val notificationIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        var large_icon = R.mipmap.ic_launcher
        try {
            //String trending = remoteMessage.getNotification().getBody().split(" ")[2];
            val trending = remoteMessage.data["body"].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]

            if (trending == "выросла")
                large_icon = R.mipmap.trending_up_notification
            else if (trending == "упала")
                large_icon = R.mipmap.trending_down_notification

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        val res = context.resources
        val builder = Notification.Builder(context)
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, large_icon))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(remoteMessage.data["title"])
                .setContentText(remoteMessage.data["body"])

        setNotificationIndicators(builder)

        val notification = Notification.BigTextStyle(builder)
                .bigText(remoteMessage.data["body"]).build()

        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        try {

            val crypto_id = remoteMessage.data["title"].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toLowerCase()
            notificationManager.notify(Codes.getCryptoCoinIndex(crypto_id), notification)

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun setNotificationIndicators(builder: Notification.Builder) {

        val sound = App.appInstance!!.preferences.notificationSound!!
        val vibrate = App.appInstance!!.preferences.notificationVibration!!

        if (sound && !vibrate)
            builder.setDefaults(Notification.DEFAULT_SOUND)
        if (!sound && vibrate)
            builder.setDefaults(Notification.DEFAULT_VIBRATE)
        if (sound && vibrate)
            builder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

    }

}
