package com.kisok.netwrokstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TAG_WIFI = "wifi_task";
    private static final int RC_PLAY_SERVICES = 123;

    public static final String TASK_TAG_PERIODIC = "periodic_task";
    private GcmNetworkManager mGcmNetworkManager;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START get_gcm_network_manager]
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);
        // [END get_gcm_network_manager]

        // BroadcastReceiver to get information from MyTaskService about task completion.
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyTaskService.ACTION_DONE)) {
                    String tag = intent.getStringExtra(MyTaskService.EXTRA_TAG);
                    int result = intent.getIntExtra(MyTaskService.EXTRA_RESULT, -1);

                    String msg = String.format("DONE: %s (%d)", tag, result);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

startWifiTask();

        // Check that Google Play Services is available, since we need it to use GcmNetworkManager
        // but the API does not use GoogleApiClient, which would normally perform the check
        // automatically.
        checkPlayServicesAvailable();
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyTaskService.ACTION_DONE);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(mReceiver);

        // For the purposes of this sample, cancel all tasks when the app is stopped.
        mGcmNetworkManager.cancelAllTasks(MyTaskService.class);
    }

//    public void startChargingTask() {
//        Log.d("", "startChargingTask");
//
//        OneoffTask task = new OneoffTask.Builder()
//                .setService(MyTaskService.class)
//                .setTag(TASK_TAG_CHARGING)
//                .setExecutionWindow(0L, 3600L)
//                .setRequiresCharging(true)
//                .build();
//
//        mGcmNetworkManager.schedule(task);
//    }

    public void startWifiTask() {
        Log.d("", "startWiFiTask");

        // Disable WiFi so the task does not start immediately
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);

        // [START start_one_off_task]
        OneoffTask task = new OneoffTask.Builder()
                .setService(MyTaskService.class)
                .setTag(TASK_TAG_WIFI)
                .setExecutionWindow(0L, 3600L)
                .setRequiredNetwork(Task.NETWORK_STATE_UNMETERED)
                .build();

        mGcmNetworkManager.schedule(task);
        // [END start_one_off_task]
    }

//    public void startPeriodicTask() {
//        Log.d("", "startPeriodicTask");
//
//        // [START start_periodic_task]
//        PeriodicTask task = new PeriodicTask.Builder()
//                .setService(MyTaskService.class)
//                .setTag(TASK_TAG_PERIODIC)
//                .setPeriod(30L)
//                .build();
//
//        mGcmNetworkManager.schedule(task);
//        // [END start_periodic_task]
//    }

    public void stopPeriodicTask() {
        Log.d("", "stopPeriodicTask");

        // [START stop_periodic_task]
        mGcmNetworkManager.cancelTask(TASK_TAG_PERIODIC, MyTaskService.class);
        // [END stop_per
    }

    private void checkPlayServicesAvailable() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                // Show dialog to resolve the error.
                availability.getErrorDialog(this, resultCode, RC_PLAY_SERVICES).show();
            } else {
                // Unresolvable error
                Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

