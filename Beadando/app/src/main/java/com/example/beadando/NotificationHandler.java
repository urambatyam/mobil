package com.example.beadando;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class NotificationHandler {
    private NotificationManager notmager;
    private Context notcontext;
    private final int NOTIFICATION_ID = 0;

    private static final String CHANNEL_ID = "Survey_filled";

    public NotificationHandler(Context notcontext) {
        this.notcontext = notcontext;
        this.notmager = (NotificationManager) notcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Survey Notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN);
        channel.setDescription("Értesités a kérdőív kitöltéséről.");
        this.notmager.createNotificationChannel(channel);
    }

    public void send(String szoveg){
        if(!this.notmager.areNotificationsEnabled()){
            Log.d(SurveyActivity.class.getName(),"nem lehet elkuldeni");
        }else {
            Log.d(SurveyActivity.class.getName(),"elll lehet kuldeni");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(notcontext,CHANNEL_ID)
                .setContentTitle("Kérdőív alaklmazás")
                .setContentText(szoveg)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        this.notmager.notify(NOTIFICATION_ID, builder.build());
    }
}
