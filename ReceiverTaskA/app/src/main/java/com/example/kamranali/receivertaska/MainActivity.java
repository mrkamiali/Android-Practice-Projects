package com.example.kamranali.receivertaska;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("MYPREF", 0);
        editText = (EditText) findViewById(R.id.toSendtext);
        button = (Button) findViewById(R.id.toSendButton);
        editText.setText(preferences.getString("tValue", ""));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                Log.d("VALUE", s);

                Intent sendIntent = new Intent();
                sendIntent.setAction("com.example.kamranali.receivertaska");
                sendIntent.putExtra("Key", s);
                sendIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(sendIntent);

                SharedPreferences.Editor saveValues = preferences.edit();
                saveValues.putString("tValue", editText.getText().toString());
                saveValues.commit();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
