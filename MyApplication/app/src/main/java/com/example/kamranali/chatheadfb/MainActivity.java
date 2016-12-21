package com.example.kamranali.chatheadfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button)findViewById(R.id.startService);
        Button stop = (Button)findViewById(R.id.stopService);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toast
                startService(new Intent(MainActivity.this, ChatHeadService.class));
            }
        });
        //stopService (from my original code)
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, ChatHeadService.class));
            }
        });
    }
}
