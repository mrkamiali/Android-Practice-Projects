package com.android.twitterauthfire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
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
    //    private Button signinButton;
    private static final String TWITTER_KEY = "h3CmFQgcZjL8nYgSXdLVBUwg8";
    private static final String TWITTER_SECRET = "hVYGwxMyK1OVXXMgcp3YNpDBpDJLS0PNLykAOeI1HLiaclPCyp";
    TwitterLoginButton mLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences twiterData;
    private SharedPreferences.Editor editor;
    private long id = 0L;
    private String name = "";
    private String profileLink;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                TWITTER_KEY,
                TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);
        twiterData = getSharedPreferences("TwitterPref", MODE_APPEND);
        editor = twiterData.edit();

        mAuth = FirebaseAuth.getInstance();
//        signinButton = (Button) findViewById(R.id.buttonSIngnin);
        mLoginButton = (TwitterLoginButton) findViewById(R.id.button_twitter_login);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("USer Found", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User NOt SIgnIn", "onAuthStateChanged:signed_out");
                }
            }
        };
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
            i.putExtra("id", twiterData.getLong("id", 0L));
            i.putExtra("link", twiterData.getString("link", ""));
            startActivity(i);


        } else {
            mLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    Log.d("session", result.data.toString());
                    handleTwitterSession(result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                    exception.printStackTrace();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void handleTwitterSession(final TwitterSession session) {
        Log.d("TwitterSession", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        Log.d("Token", "" + credential);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SignSuscessFullly DOne", "signInWithCredential:onComplete:" + task.isSuccessful());

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

                                    i.putExtra("name", twiterData.getString("name", ""));
                                    i.putExtra("id", twiterData.getLong("id", 0L));
                                    i.putExtra("link", twiterData.getString("link", ""));
                                    startActivity(i);

                                } catch (TwitterException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void failure(TwitterException exception) {

                            }
                        });
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Log.w("SIgnin Failed", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
