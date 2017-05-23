package com.android.twitterauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "SH6EpdQhK0Fus7JRlaEjdepfW";
    private static final String TWITTER_SECRET = "RS9IID3f05V6DZIjpznBdJkT7oUAhHiIS55Pc2FvFoSoS6EhyX";
    private SharedPreferences twiterData;
    private TwitterLoginButton loginButton;
    private TextView status;
    private String msg;
    private String profileImage;
    private long id = 0L;
    private String name = "";
    private String profileLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        final SharedPreferences twiterData = getSharedPreferences("TwitterPref", MODE_APPEND);
        final SharedPreferences.Editor editor = twiterData.edit();

        Log.d("RequestCode", "" + authConfig.getRequestCode());


        status = (TextView) findViewById(R.id.status);
        status.setText("Status: Ready");
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        String secret = "";

        if (Twitter.getSessionManager().getActiveSession() != null) {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();

            Log.d("Session", "" + session);
            TwitterAuthToken authToken = session.getAuthToken();
            Log.d("authToken", "" + session);
            String token = authToken.token;
            Log.d("token", "" + session);
            secret = authToken.secret;
            Log.d("secret", "" + session);
            Log.d("BooleanValue", "" + Boolean.valueOf(secret = null));

//            Log.d("Name", session.getUserName().toString());
//            Log.d("USerID", "" + session.getUserId());
//            Log.d("profile KA link",profileImage);

            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            i.putExtra("name", twiterData.getString("name", ""));
            i.putExtra("id", twiterData.getLong("id", id));
            i.putExtra("link", twiterData.getString("link", ""));
            startActivity(i);


        } else {


            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {


                    final TwitterSession session = result.data;
                    Twitter.getApiClient(session).getAccountService().verifyCredentials(true, true).enqueue(new Callback<User>() {
                        @Override
                        public void success(Result<User> result) {
                            try {
                                User user = result.data;
                                id = user.getId();
                                name = user.name.toString();
                                profileLink = user.profileImageUrl.toString();

                                Log.d("DATA", "NAme is   : " + user.name.toString() + "\n ID = " + user.getId() + "\n Profile PicLink" + user.profileImageUrl);

                                editor.putString("name", name);
                                editor.putLong("id", id);
                                editor.putString("link", profileLink);
                                editor.commit();

                                Intent i = new Intent(MainActivity.this, SecondActivity.class);

                                i.putExtra("name", twiterData.getString("name", name));
                                i.putExtra("id", twiterData.getLong("id", id));
                                i.putExtra("link", twiterData.getString("link", profileLink));
                                startActivity(i);

                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });

                    msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
