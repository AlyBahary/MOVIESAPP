package com.beetleware.ugt.network.services.Notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.beetleware.ugt.R;
import com.beetleware.ugt.ui.activities.SplashActivity;
import com.beetleware.ugt.utils.Config;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "FCM_Service";

    private onmsgRecived recived;


    public MyFirebaseMessagingService(onmsgRecived recived) {
        this.recived = recived;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().isEmpty())
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        else
            showNotification(remoteMessage.getData());

        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d(TAG, "onMessageReceived: " + cn.getClassName());
//        if (cn.getClassName().contains("MeshwarChatActivity")) {
//            notifyMessage(remoteMessage.getData().get("request_id"));
//        } else if (cn.getClassName().contains("ChatActivity")) {
//            notifyMessage(remoteMessage.getData().get("request_id"));
//        }

    }


    private void showNotification(Map<String, String> data) {


        String title = data.get("title").toString();
        String body = "";
        try {
            body = data.get("body").toString();
        } catch (Exception e) {

        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATIO_CHANEL_ID = "com.beetleware.ugt";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATIO_CHANEL_ID, "notification"
                    , NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATIO_CHANEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(this, R.color.primaryGreen))
                .setContentInfo("Info");
        Map<String, String> notificationMessage = data;
        if (notificationMessage.containsKey("targetScreen")) {
            Intent resultIntent;
            {
                resultIntent = new Intent(this, SplashActivity.class);

            }
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SplashActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(resultPendingIntent);
            notificationBuilder.setAutoCancel(true);


        } else {
            Log.d(TAG, "onMessageReceived: notContained");
            Intent resultIntent = new Intent(this, SplashActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SplashActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(resultPendingIntent);
            notificationBuilder.setAutoCancel(true);

        }

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATIO_CHANEL_ID = "com.beetleware.ugt";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATIO_CHANEL_ID, "notification"
                    , NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATIO_CHANEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(this, R.color.primaryGreen))
                .setContentInfo("Info");
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
        SharedPreferences sharedpreferences = getSharedPreferences(Config.PREFS_NAME, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Config.FIREBASE_TOKEN, "" + s);
        editor.commit();
        // Hawk.put(Constants.TOKEN, s);
    }


    private void notifyMessage(String request_id) {
        Log.d("sender", "Broadcasting notifyMessage");
        Intent intent = new Intent("notifyMessage");
        // You can also include some extra data.
        intent.putExtra("request_id", request_id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void notifySessionEnded() {
        Log.d("sender", "Broadcasting notifySessionEnded");
        Intent intent = new Intent("notifySessionEnded");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
