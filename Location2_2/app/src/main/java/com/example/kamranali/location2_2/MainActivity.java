package com.example.kamranali.location2_2;

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
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<Status> {

    private TextView mStatusText;
    private Button requestUpdateButton,
            removeUpdatesButton;
    private GoogleApiClient mGoolgeApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    //BroadCastReceiverClass Variable
    public ActivityDetectionBroadCastReciever broadCastReciever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusText = (TextView) findViewById(R.id.detectedActivities);
        requestUpdateButton = (Button) findViewById(R.id.request_activity_updates_button);
        removeUpdatesButton = (Button) findViewById(R.id.remove_activity_updates_button);
        broadCastReciever = new ActivityDetectionBroadCastReciever();
        buildGoogleApiClient();

    }

    public void removeActivityUpdatesButtonHandler(View view) {
        if (!mGoolgeApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        // Remove all activity updates for the PendingIntent that was used to request activity
        // updates.
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                mGoolgeApiClient,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdateButton.setEnabled(true);
        removeUpdatesButton.setEnabled(false);
        mStatusText.setText("");
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        if (!mGoolgeApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoolgeApiClient,
                Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdateButton.setEnabled(false);
        requestUpdateButton.setEnabled(true);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private synchronized void buildGoogleApiClient() {
        mGoolgeApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onStart() {
        mGoolgeApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoolgeApiClient.isConnected()) {
            mGoolgeApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        //unRegister the Receiver that was register on startup
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReciever);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReciever,
                new IntentFilter(Constants.BROADCAST_ACTION));
        super.onResume();
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

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d("", "SuccessFully added Activity Detection ");
        } else {
            Log.d("", "Error adding or removing Activity Detection " + status.toString());

        }
    }

    public class ActivityDetectionBroadCastReciever extends BroadcastReceiver {
        public static final String TAG = "receiver";

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<DetectedActivity> updatedActivity = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            String status = "";
            for (DetectedActivity thisActivity : updatedActivity) {
                status += getActivityString(thisActivity.getType()) + " " + thisActivity.getConfidence() + " %\n";
            }
            mStatusText.setText(status);

        }
    }
}
