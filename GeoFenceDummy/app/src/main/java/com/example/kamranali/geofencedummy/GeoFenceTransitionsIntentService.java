package com.example.kamranali.geofencedummy;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamranali on 03/01/2017.
 */

public class GeoFenceTransitionsIntentService extends IntentService {
    public static final String TAG = "GeoFenceTransitionsIntentService";

    public GeoFenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            String errorMessage = GeofenceErrorMessages.getErrorString(this,geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            List<Geofence> triggerGeofence = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition,triggerGeofence);
            sendnotification(geofenceTransitionDetails);
            Log.d(TAG,geofenceTransitionDetails);

        }else {
            Log.d(TAG,getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }

    }

    private String getGeofenceTransitionDetails(GeoFenceTransitionsIntentService geoFenceTransitionsIntentService,
                                                int geofenceTransition,
                                                List<Geofence> triggerGeofence) {

        String geoFenceTransitionString = getTransitionString(geofenceTransition);
        ArrayList triggringGeofenceIdsList =  new ArrayList();
        for (Geofence geofence:triggerGeofence){
            triggringGeofenceIdsList.add(geofence.getRequestId());
        }
        String trigringGeoFenceIdsString = TextUtils.join(" ",triggringGeofenceIdsList);

        return geoFenceTransitionString + " : "+trigringGeoFenceIdsString ;
    }

    private String getTransitionString(int geofenceTransition) {
        switch (geofenceTransition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
    private void sendnotification(String notificationDetails){
        //Create an explicit Intent Notification
        Intent intenNotification = new Intent(getApplicationContext(),MainActivity.class);

        //construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        //Add the MaiActivity to the Task Stack
        stackBuilder.addParentStack(MainActivity.class);

        //Push the content Intent into the stack
        stackBuilder.addNextIntent(intenNotification);

        //Get a pending Intent
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        //Get a notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //Define a notification content
        builder.setSmallIcon(R.mipmap.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
        .setColor(Color.RED)
        .setContentTitle(notificationDetails)
        .setContentText("Click Notifiaction to return to app")
        .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(false);

        //Get an instance of the NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



    }
}
