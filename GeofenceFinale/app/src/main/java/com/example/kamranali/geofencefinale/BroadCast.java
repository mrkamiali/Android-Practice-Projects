package com.example.kamranali.geofencefinale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Created by kamranali on 13/01/2017.
 */

public class BroadCast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, GeoFenceTransitionService.class);
        serviceIntent.putExtra(Constants.SERVICE_EXTRAS,true);
        context.startService(serviceIntent);
    }

}
