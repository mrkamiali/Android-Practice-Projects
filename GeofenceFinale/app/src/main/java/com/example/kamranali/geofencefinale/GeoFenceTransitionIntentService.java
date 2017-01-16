package com.example.kamranali.geofencefinale;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kamranali on 13/01/2017.
 */

public class GeoFenceTransitionIntentService extends IntentService {
    private static final String TAG = "GeoFenceTransitionIntentService.Service";

    public GeoFenceTransitionIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            String errorCode = GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            Log.d(TAG, " " + geofencingEvent.getErrorCode());
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        //Checking geofenceTranstion type
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            //Getting List of the geofences that triggered this geofence transition alert
            List<Geofence> triggerdGeoFencesList = geofencingEvent.getTriggeringGeofences();

            //Getting the location that triggered the geofence transition.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggerdGeoFencesList);

            sendNotification(geofenceTransitionDetails);
            Log.d(TAG, geofenceTransitionDetails);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy , hh:mm:ss", Locale.ENGLISH);
            String st_date = format.format(date);

        } else {
            // Log the error.
            Log.d(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }

    }

    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofencesList) {
        String geofenceTransitionString = getTransitionString(geofenceTransition);

        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofencesList) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setColor(Color.RED)
                .setContentText("Click notification to return to app")
                .setContentTitle(notificationDetails)
                .setContentIntent(notificationPendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
}
