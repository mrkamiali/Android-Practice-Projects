package com.example.kamranali.broadcastrestart;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kamranali on 10/01/2017.
 */

public class BroadCastt extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String string = extras.getString(Constants.BROADCAST_Intent);

        Toast.makeText(context, "beforeRestart123456 " , Toast.LENGTH_SHORT).show();

//        String action = intent.getAction();
//
//        Log.i("Receiver", "Broadcast received: " + action);
//
//        if(action.equals("my.action.string")){
//            String state = intent.getExtras().getString("extra");
//            Toast.makeText(context,"Data Received "+state,Toast.LENGTH_LONG).show();
//
//        }
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String a = prefs.getString("Key1", null);
//        String b = prefs.getString("Key2", null);


        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

//            Toast.makeText(context, "AfterRestart " + a, Toast.LENGTH_LONG).show();
//            Toast.makeText(context, "AfterRestart " + b, Toast.LENGTH_LONG).show();
//
//            Intent intent1 = new Intent(context, myService.class);
//            intent1.putExtra("Key1", a);
//            intent1.putExtra("Key2", b);
//
//            context.startService(intent1);


//            ArrayList<String> parcelableArrayListExtra = (ArrayList<String>) intent.getParcelableArrayListExtra(Constants.BROADCAST_Intent);

            if (string!=null){

                Toast.makeText(context, "AfterRestart "+string , Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(context, "AfterRestart ", Toast.LENGTH_SHORT).show();
            }



        }else {
            Toast.makeText(context, "beforeRestart " , Toast.LENGTH_LONG).show();
        }
    }
}
