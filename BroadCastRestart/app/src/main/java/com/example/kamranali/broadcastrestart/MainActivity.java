package com.example.kamranali.broadcastrestart;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BroadCastt broadCastt;
    private static final String TAG = "MAINACTIVITY";
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient googleApiClient;
    private Button addGeofencesButton;
    PendingIntent mGeofencePendingIntent;
    private SharedPreferences mSharedPreferences;
    private boolean mGeofencesAdded;
    private ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadCastt = new BroadCastt();
        arrayList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            arrayList.add("Kamran " + i);
        }
        Intent intent = new Intent(this,BroadCastt.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constants.BROADCAST_Intent, "Kamran");
        intent.putExtras(bundle);

        registerReceiver(broadCastt, new IntentFilter("android.intent.action.BOOT_COMPLETED"));

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.putString("Key1", "Kamran");
//        edit.putString("Key2", "Faiz");
//        edit.commit();
//        Toast.makeText(MainActivity.this,"DataStored ",Toast.LENGTH_SHORT).show();

        sendBroadcast(intent);

//        addGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
//        // Empty list for storing geofences.
//        mGeofenceList = new ArrayList<Geofence>();
//
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
//
//        populateGeofenceList();
//        buildGoogleApiClient();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).sendBroadcast(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //    private synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    private void populateGeofenceList() {
//        for (Map.Entry<String, LatLng> entry : Constants.PanacloudLatLong.entrySet()) {
//
//            Log.d(TAG, "ENTERYSET" + entry.getKey() + " Value " + entry.getValue());
//            Toast.makeText(MainActivity.this, entry.getKey() + " Value " + entry.getValue(), Toast.LENGTH_SHORT).show();
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
//    }
//    public synchronized void addGeofencesButtonHandler(View view) {
//        if (!googleApiClient.isConnected()) {
//            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//
//            LocationServices.GeofencingApi.addGeofences(
//                    googleApiClient,
//                    // The GeofenceRequest object.
//                    getGeofencingRequest(),
//                    // A pending intent that that is reused when calling removeGeofences(). This
//                    // pending intent is used to generate an intent when a matched geofence
//                    // transition is observed.
//                    getGeofencePendingIntent()
//            ).setResultCallback(this); // Result processed in onResult().
//        } catch (SecurityException securityException) {
//            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
//            Log.d(TAG,securityException.getMessage());
//        }
//    }
//    private GeofencingRequest getGeofencingRequest() {
//        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
//        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
//        builder.addGeofences(mGeofenceList);
//        return builder.build();
//    }
//    private PendingIntent getGeofencePendingIntent() {
//        if (mGeofencePendingIntent!=null){
//            return mGeofencePendingIntent;
//        }
//
//        Intent intent = new Intent(this, BroadCastt.class);
//        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//    @Override
//    protected void onStop() {
//        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onStart() {
//        if (!googleApiClient.isConnecting() || !googleApiClient.isConnected()) {
//            googleApiClient.connect();
//        }
//        super.onStart();
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Log.d(TAG, "Connected to GoogleApiClient");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.d(TAG, "Connection suspended");
//        googleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.d (TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorMessage());
//    }
//
//    @Override
//    public void onResult(@NonNull Status status) {
//        if (status.isSuccess()) {
//            mGeofencesAdded= !mGeofencesAdded;
//            Toast.makeText(
//                    this,
//                    "Geofences Added",
//                    Toast.LENGTH_SHORT
//            ).show();
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY,mGeofencesAdded);
//            editor.commit();
//        } else {
//            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    status.getStatusCode());
//        }
//    }
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }


}
