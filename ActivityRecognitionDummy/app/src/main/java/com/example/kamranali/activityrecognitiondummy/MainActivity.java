package com.example.kamranali.activityrecognitiondummy;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>{
    private static final String TAG = "MainActivity";

    private TextView mstatusTextView;
    private Button requestUpdatesButton, removeUpdatesButton;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    protected ActivityDetectionBroadCastReceiver mbroadCastReceiver;
    private PendingIntent activityDetectionPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mstatusTextView = (TextView) findViewById(R.id.detectedActivities);
        requestUpdatesButton = (Button) findViewById(R.id.request_activity_updates_button);
        removeUpdatesButton = (Button) findViewById(R.id.remove_activity_updates_button);
        mbroadCastReceiver = new ActivityDetectionBroadCastReceiver();
        buildGoogleApiClient();
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()){
            Log.d(TAG,"CallBAck Succes Called "+status);
        }else {
            Log.d(TAG," CallBack Failed Called "+status.getStatusMessage());
        }
    }

    public PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this,DetectedActivitiesIntentService.class);

        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public class ActivityDetectionBroadCastReceiver extends BroadcastReceiver {
        private static final String TAG = "MainActivity.BroadCastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> parcelableArrayListExtra = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            String strstatus = "";
            for (DetectedActivity thisActivity : parcelableArrayListExtra) {
                strstatus += getActivityString(thisActivity.getType()) + " : " + thisActivity.getConfidence() + " %\n";
            }
            mstatusTextView.setText(strstatus);
        }
    }

    public String getActivityString(int detectedActivityType) {
        Resources resources = this.getResources();
        switch (detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this,
                    R.string.not_connected, Toast.LENGTH_LONG).show();
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                googleApiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdatesButton.setEnabled(false);
        removeUpdatesButton.setEnabled(true);
    }
    public void removeActivityUpdatesButtonHandler(View view){
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this,
                    R.string.not_connected, Toast.LENGTH_LONG).show();
        }
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                googleApiClient,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdatesButton.setEnabled(true);
        removeUpdatesButton.setEnabled(false);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected Called.");
//        locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(1000);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended Called.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed Called.");
    }

    @Override
    protected void onStart() {
            googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mbroadCastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mbroadCastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }
}
