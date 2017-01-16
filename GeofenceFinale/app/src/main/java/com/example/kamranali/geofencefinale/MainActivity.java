package com.example.kamranali.geofencefinale;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleApiClient googleApiClient;
    PendingIntent mGeofencePendingIntent;
    private boolean mGeofencesAdded;
    private Button addGeofencesButton,sendToFireBase;
    private DatabaseReference firebaseDatabase;
    private Model model;
    private BroadCast broadCast;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        broadCast = new BroadCast();
        model = new Model();

        addGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        sendToFireBase = (Button) findViewById(R.id.firebase_button);
//        mGeofenceList = new ArrayList<Geofence>();
//        model = new Model();

        registerReceiver(broadCast,new IntentFilter("android.intent.action.BOOT_COMPLETED"));

        sendToFireBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            for (Map.Entry<String, LatLng> entry : Constants.PanacloudLatLong.entrySet()) {
            Log.d(TAG, "ENTERYSET :" + entry.getKey() + " Value " + entry.getValue());

            String key = firebaseDatabase.child(Constants.FENCES_NODE).push().getKey();
            LatLng value = entry.getValue();
            firebaseDatabase.child(Constants.FENCES_NODE).child(key)
                    .setValue(new Model(value.longitude,value.latitude,Constants.GEOFENCE_RADIUS_IN_METERS,key,entry.getKey()));
        }
                firebaseDatabase.child(Constants.FENCES_NODE).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ab : dataSnapshot.getChildren()) {
                            Model value = ab.getValue(Model.class);
                            dataBase.writeFences(value);
                            Log.d(TAG, value.toString());
                            Toast.makeText(MainActivity.this,"ValueStored to DB",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        addGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BroadCast.class);
               sendBroadcast(intent);
            }
        });

//        populateGeofenceList();
//        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadCast);
    }
    //    private synchronized void  buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    public void populateGeofenceList() {
//        for (Map.Entry<String, LatLng> entry : Constants.PanacloudLatLong.entrySet()) {
//
//            Log.d(TAG, "ENTERYSET :" + entry.getKey() + " Value " + entry.getValue());
//            Toast.makeText(MainActivity.this, entry.getKey() + " Value " + entry.getValue(), Toast.LENGTH_SHORT).show();
//
//            String key = firebaseDatabase.child(Constants.FENCES_NODE).push().getKey();
//            if (key!=null){
//                model.setPushkey(key);
//            }
//            LatLng value = entry.getValue();
//            firebaseDatabase.child(Constants.FENCES_NODE).child(model.getPushkey()).setValue(new Model(value.longitude,value.latitude,Constants.GEOFENCE_RADIUS_IN_METERS,model.getPushkey()));
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
//
//    private GeofencingRequest getGeofencingRequest() {
//        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
//        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
//        builder.addGeofences(mGeofenceList);
//        return builder.build();
//    }
//
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
//
    //    private PendingIntent getGeofencePendingIntent() {
//        if (mGeofencePendingIntent!=null){
//            return mGeofencePendingIntent;
//        }
//
//        Intent intent = new Intent(this, GeoFenceTransitionIntentService.class);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
//        } else {
//            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    status.getStatusCode());
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
}
