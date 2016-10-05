package com.example.kamranali.receivertaskb;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MyBroadcastReceiver myReceiver;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("MYPREF",Context.MODE_WORLD_READABLE);
        textView = (TextView) findViewById(R.id.toReceivetext);

        if (preferences.getString("tValue","")!=null){
                        textView.setText(preferences.getString("tValue",""));
        }


        IntentFilter intentFilter = new IntentFilter("com.example.kamranali.receivertaska");
        myReceiver = new MyBroadcastReceiver();

        if (intentFilter != null) {
            registerReceiver(myReceiver, intentFilter);
        }


    }

    public class MyBroadcastReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String key = intent.getStringExtra("Key");
            Log.d("KEY", "VALUE :" + key);
            textView.setText(key);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("tValue",key);
            editor.commit();
            Toast.makeText(MainActivity.this, "Data Received from External App", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (myReceiver != null)
            unregisterReceiver(myReceiver);

    }
}

