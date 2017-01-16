package com.example.kamranali.activityrecognitiondummy;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamranali on 04/01/2017.
 */

public class DetectedActivitiesIntentService extends IntentService {

    private static final String TAG = "DetectedActivitiesIntentService";

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        ArrayList<DetectedActivity> probableActivities = (ArrayList) result.getProbableActivities();
        localIntent.putExtra(Constants.ACTIVITY_EXTRA,probableActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
