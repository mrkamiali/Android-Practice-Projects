package com.example.kamranali.geofenceprojectbroadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by kamranali on 06/01/2017.
 */

public class GeoFenceTransitionBroadCast extends BroadcastReceiver {
    private static final String TAG = "GeoFenceTransitionBroadCast.Service";

    public GeoFenceTransitionBroadCast() {
        super();
    }

    private DatabaseReference firebaseDatabase;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        Intent broadcastIntent = new Intent(Constants.BROADCAST_ACTION);
        broadcastIntent.addCategory(Constants.CATEGORY_LOCATION_SERVICES);


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorCode = GeofenceErrorMessages.getErrorString(context, geofencingEvent.getErrorCode());
            Log.d(TAG, " " + geofencingEvent.getErrorCode());
            return;
        } else {
            //Getting type of Transition Occur
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            //Checking geofenceTranstion type
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                ArrayList<Geofence> triggerdGeoFencesList = (ArrayList) geofencingEvent.getTriggeringGeofences();

                //Getting the location that triggered the geofence transition.
                String geofenceTransitionDetails = getGeofenceTransitionDetails(context, geofenceTransition, triggerdGeoFencesList);


                sendNotification(geofenceTransitionDetails);
                broadcastIntent.putExtra(Constants.ACTIVITY_EXTRA, triggerdGeoFencesList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

                Log.d(TAG, geofenceTransitionDetails);
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy , hh:mm:ss", Locale.ENGLISH);
                String st_date = format.format(date);
                Map<String, String> timestamp = ServerValue.TIMESTAMP;
                Log.d(TAG, " keySet " + timestamp.keySet() + " entrySet " + timestamp.entrySet() + " toString " + timestamp.toString() + " values " + timestamp.values() + " size " + timestamp.size());

                HashMap<String, Object> value = new HashMap<>();
                value.put("longitude", geofencingEvent.getTriggeringLocation().getLongitude());
                value.put("lttitde", geofencingEvent.getTriggeringLocation().getLatitude());
                value.put("place", geofenceTransitionDetails);
                value.put("trigered-Time", st_date);
                value.put("server-Time", ServerValue.TIMESTAMP);
                firebaseDatabase.child("Kamran").push().setValue(value);

            } else {
                // Log the error.
                Log.d(TAG, context.getString(R.string.geofence_transition_invalid_type, geofenceTransition));

            }
        }

    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        //Getting Data form Intent
//        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
//
//        //Checking if theres any error
//        if (geofencingEvent.hasError()) {
//            String errorCode = GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
//            Log.d(TAG, " " + geofencingEvent.getErrorCode());
//            return;
//        }
//        //Getting type of Transition Occur
//        int geofenceTransition = geofencingEvent.getGeofenceTransition();
//
//        //Checking geofenceTranstion type
//        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
//
//            //Getting List of the geofences that triggered this geofence transition alert
//            List<Geofence> triggerdGeoFencesList = geofencingEvent.getTriggeringGeofences();
//
//            //Getting the location that triggered the geofence transition.
//            String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggerdGeoFencesList);
//
//            sendNotification(geofenceTransitionDetails);
//            Log.d(TAG, geofenceTransitionDetails);
//        } else {
//            // Log the error.
//            Log.d(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
//        }
//
//    }

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
                return context.getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return context.getString(R.string.geofence_transition_exited);
            default:
                return context.getString(R.string.unknown_geofence_transition);
        }
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setColor(Color.RED)
                .setContentText("Click notification to return to app")
                .setContentTitle(notificationDetails)
                .setContentIntent(notificationPendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
}
