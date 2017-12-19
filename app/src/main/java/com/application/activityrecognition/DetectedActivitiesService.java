package com.application.activityrecognition;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Suvam on 12/18/2017.
 */

public class DetectedActivitiesService extends IntentService {

    public static final String TAG = DetectedActivitiesService.class.getSimpleName();

    public DetectedActivitiesService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult activityRecognitionResult = ActivityRecognitionResult.extractResult(intent);
        ArrayList<DetectedActivity> detectedActivities = (ArrayList<DetectedActivity>) activityRecognitionResult.getProbableActivities();

        for(DetectedActivity activity : detectedActivities){
            Log.d(TAG,"Suvam : Detected Activity : " + activity.getType() + "; " + activity.getConfidence());
            sendBroadcastActivity(activity);
        }
    }

    private void sendBroadcastActivity(DetectedActivity activity){
        Intent intent = new Intent(ActivityConstants.BROADCAST_DETECTED_ACTIVITY);
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}