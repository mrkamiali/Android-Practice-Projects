package com.example.kamranali.geofencefinale;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamranali on 13/01/2017.
 */

public class GeoFenceTransitionService extends Service implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private static final String TAG = "TransitionService";
    protected ArrayList<Geofence> mGeofenceList;
    PendingIntent mGeofencePendingIntent;
    private DatabaseReference firebaseDatabase;
    private Model model;
    private boolean mGeofencesAdded;
    private DataBase dataBase;

    private static GeoFenceTransitionService ourInstance;


    public GeoFenceTransitionService() {

    }

    //1st
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "OnCreat ", Toast.LENGTH_LONG).show();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mGeofenceList = new ArrayList<Geofence>();
        model = new Model();
        dataBase = new DataBase(this);

    }

    public void geofenceHandler() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.d(TAG, securityException.getMessage());
        }
    }

    private boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }

        Intent intent = new Intent(this, GeoFenceTransitionIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }

    private void populateGeofenceList() {
        List<Model> models = dataBase.reterivingList();

        for (Model model1 : models) {
            Toast.makeText(this, "Initilizing ", Toast.LENGTH_LONG).show();
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(model1.getPlaceName())
                    .setCircularRegion(
                            model1.getLatitude(),
                            model1.getLongitued(),
                            model1.getRadius())
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build());
        }


//        for (Map.Entry<String, LatLng> entry : Constants.PanacloudLatLong.entrySet()) {
//
//            Log.d(TAG, "ENTERYSET :" + entry.getKey() + " Value " + entry.getValue());
//            Toast.makeText(this, entry.getKey() + " Value " + entry.getValue(), Toast.LENGTH_SHORT).show();
//
////            String key = firebaseDatabase.child(Constants.FENCES_NODE).push().getKey();
////            if (key!=null){
////                model.setPushkey(key);
////            }
////            LatLng value = entry.getValue();
////            firebaseDatabase.child(Constants.FENCES_NODE).child(model.getPushkey())
////                    .setValue(new Model(value.longitude,value.latitude,Constants.GEOFENCE_RADIUS_IN_METERS,model.getPushkey(),entry.getKey()));
////
//
//            mGeofenceList.add(new Geofence.Builder()
//                    // Set the request ID of the geofence. This is a string to identify this
//                    // geofence.
//                    .setRequestId(entry.getKey())
//
//                    // Set the circular region of this geofence.
//                    .setCircularRegion(
//                            entry.getValue().latitude,
//                            entry.getValue().longitude,
//                            Constants.GEOFENCE_RADIUS_IN_METERS
//                    )
//
//                    // Set the expiration duration of the geofence. This geofence gets automatically
//                    // removed after this period of time.
//                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
//
//                    // Set the transition types of interest. Alerts are only generated for these
//                    // transition. We track entry and exit transitions in this sample.
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                            Geofence.GEOFENCE_TRANSITION_EXIT)
//                    //set Dwell Time
////                    .setLoiteringDelay(1000*5)
//
//                    // Create the geofence.
//                    .build());
//        }
    }

    //2nd
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (googleApiClient == null) {
            buildGoogleApiClient();
            googleApiClient.connect();
        } else if (!googleApiClient.isConnected() || googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
        if (!googleApiClient.isConnected()) {

            googleApiClient.connect();
        }
        if (googleApiClient.isConnected()) {
            Toast.makeText(this, "ApiConnected", Toast.LENGTH_LONG).show();

            populateGeofenceList();
            geofenceHandler();

        }
        Toast.makeText(this, "ServiceStarted ", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    //3rd
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            mGeofencesAdded = !mGeofencesAdded;
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }
}
