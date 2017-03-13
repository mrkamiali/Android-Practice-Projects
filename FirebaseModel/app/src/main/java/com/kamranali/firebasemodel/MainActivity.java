package com.kamranali.firebasemodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kamranali.firebasemodel.fragment.Home_Fragment;
import com.kamranali.firebasemodel.signupfragment.Signup_fragment;

public class MainActivity extends AppCompatActivity {
    private Button signup_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Home_Fragment()).commit();
        }

    }
}
