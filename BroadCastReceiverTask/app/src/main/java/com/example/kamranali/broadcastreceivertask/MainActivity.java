package com.example.kamranali.broadcastreceivertask;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private String myString;
    SharedPreferences tosave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editView);
        button = (Button) findViewById(R.id.buttonToSend);
        tosave = getSharedPreferences("MYPREF", 0);

        editText.setText(tosave.getString("tosave1", ""));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myString = editText.getText().toString();
                Log.d("sadsa", myString);
                Intent intent = new Intent();
                intent.setAction("com.example.kamranali.broadcastreceivertask");
                intent.putExtra("abc", myString);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//                intent.setComponent(new ComponentName("com.example.kamranali", "com.example.kamranali.broadcastreceiverb"));
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("editTextValue ", editText.getText().toString());
      //  SharedPreferences preferences = getSharedPreferences("MYPREF", 0);
      //  SharedPreferences.Editor pref = preferences.edit();
      //  pref.putString("tosave1", editText.getText().toString());
     //   pref.commit();
    }
}
