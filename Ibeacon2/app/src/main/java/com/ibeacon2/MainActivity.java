package com.ibeacon2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BeaconManager beaconManager;
    List<String> beaconsList;
    private Region region;
    private TextView textView;
    BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }

        beaconsList = new ArrayList<>();
        beaconsList.add("FDA50693-A4E2-4FB1-AFCF-C6EB07647825");
        beaconsList.add("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);


    }
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
    @Override
    public void onBeaconServiceConnect() {

        final Region region1 = new Region("beacon1",Identifier.parse("FDA50693-A4E2-4FB1-AFCF-C6EB07647825"),null,null);
        final Region region2 = new Region("beacon2",Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),null,null);

        try {

            beaconManager.startMonitoringBeaconsInRegion(region1);
            beaconManager.startMonitoringBeaconsInRegion(region2);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                for (Beacon beacon : beacons) {
                    Log.d(TAG, "didRangeBeaconsInRegion: UUID: " + beacon.getId1()
                            + "\nMAJOR: " + beacon.getId2()
                            + "\nMINOR: " + beacon.getId3()
                            + "\nRSSI: " + beacon.getRssi()
                            + "\nTX: " + beacon.getTxPower()
                            + "\nDISTANCE: " + beacon.getDistance());
                }
            }
        });

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                    Log.d(TAG, "didEnterRegion: " + region.getId1());
//                    updateLog("Entered in "+region.getUniqueId());
                try {
                    beaconManager.startRangingBeaconsInRegion(region1);
                    beaconManager.startRangingBeaconsInRegion(region2);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                    Log.d(TAG, "didExitRegion: " + region.getId1());
//                    updateLog("Exited From "+region.getUniqueId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {


            }
        });

//        beaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                for (Beacon beacon : beacons) {
//                    Log.d(TAG, "didRangeBeaconsInRegion: distance :" + beacon.getDistance() + " / UUID " + beacon.getId1() + "/ Major " + beacon.getId2() + "/ Minor " + beacon.getId3());
//                    Log.d(TAG, "didRangeBeaconsInRegion: List"+beacon.getParserIdentifier());
//                    Log.d(TAG, "didRangeBeaconsInRegion: List"+beacon.getIdentifiers());
//                }
//            }
//        });

    }

    private void updateLog(final String logMessage)
    {
         String logText = this.textView.getText().toString();

        this.textView.setText(logMessage + "\n\n" + logText);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
}
