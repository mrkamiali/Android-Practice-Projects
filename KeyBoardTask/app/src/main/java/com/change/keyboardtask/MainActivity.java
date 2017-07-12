package com.change.keyboardtask;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textview;
    private EditText editText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        textview = (TextView) findViewById(R.id.textview);
        textview.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview:
                String language = Locale.getDefault().getLanguage();
                String displayLanguage = Locale.getDefault().getDisplayLanguage();

                Toast.makeText(MainActivity.this, "language ("+language+") \nDisp ("+displayLanguage, Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }
}
