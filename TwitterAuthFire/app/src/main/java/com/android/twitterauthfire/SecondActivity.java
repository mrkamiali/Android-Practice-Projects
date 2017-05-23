package com.android.twitterauthfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;

public class SecondActivity extends AppCompatActivity {
    Button logout_Button;
    private TextView userNAme, iD;
    private ImageView imgView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mAuth = FirebaseAuth.getInstance();

        userNAme = (TextView) findViewById(R.id.userName);
        iD = (TextView) findViewById(R.id.iD);
        logout_Button = (Button) findViewById(R.id.logout);
        imgView = (ImageView) findViewById(R.id.imgView);

        Bundle extras = getIntent().getExtras();
        String username = extras.get("name").toString();
        Log.d("userNAme 2", username);
        long userId = extras.getLong("id", 0L);
        Log.d("userId 2", ""+userId);
        String link = extras.get("link").toString();
        Log.d("Link", link);


        userNAme.setText(username);
        iD.setText(String.valueOf(userId));
        Picasso.with(this).load(link).resize(150, 150).centerCrop().into(imgView);

        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Twitter.getSessionManager().clearActiveSession();
                mAuth.signOut();
                Twitter.logOut();
                Intent i = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
