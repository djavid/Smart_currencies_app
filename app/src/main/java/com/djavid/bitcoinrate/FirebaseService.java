package com.djavid.bitcoinrate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.view.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;


public class FirebaseService extends FirebaseMessagingService {

    private static final int NOTIFY_ID = 101;


    public FirebaseService() { }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            showNotification(remoteMessage);

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            showNotification(remoteMessage);
        }
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    private void showNotification(RemoteMessage remoteMessage) {

        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int large_icon = R.mipmap.ic_launcher;
        try {
            //String trending = remoteMessage.getNotification().getBody().split(" ")[2];
            String trending = remoteMessage.getData().get("body").split(" ")[2];

            if (trending.equals("выросла"))
                large_icon = R.mipmap.trending_up_notification;
            else if (trending.equals("упала"))
                large_icon = R.mipmap.trending_down_notification;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context)
                .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, large_icon))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody());
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"));

        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(remoteMessage.getData().get("body")).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        try {

            String crypto_id = remoteMessage.getData().get("title").split(" ")[0].toLowerCase();
            notificationManager.notify(Codes.getCryptoCoinIndex(crypto_id), notification);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
