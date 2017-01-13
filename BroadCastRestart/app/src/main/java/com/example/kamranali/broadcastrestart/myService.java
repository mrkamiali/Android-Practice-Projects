package com.example.kamranali.broadcastrestart;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by kamranali on 10/01/2017.
 */

public class myService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        Intent intents = new Intent(getBaseContext(),MainActivity.class);
//        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intents);

        Toast.makeText(this,"ServiceStarted",Toast.LENGTH_LONG).show();

        return Service.START_NOT_STICKY;

    }
}
