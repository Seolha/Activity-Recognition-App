package com.application.activityrecognition;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Suvam on 12/18/2017.
 */

public class DetectActivitiesBackgroundService extends Service {
    private static final String TAG = DetectActivitiesBackgroundService.class.getSimpleName();

    //Activity Recognition fields
    private ActivityRecognitionClient activityRecognitionClient;
    private Intent intentService;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DetectActivitiesBackgroundService.LocalBinder();
    }

    public class LocalBinder extends Binder {
        public DetectActivitiesBackgroundService getService(){
            return DetectActivitiesBackgroundService.this;
        }
    }

    public DetectActivitiesBackgroundService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityRecognitionClient = new ActivityRecognitionClient(getApplicationContext());
        intentService = new Intent(this, DetectedActivitiesService.class);
        pendingIntent = PendingIntent.getService(this, 1, intentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdatesHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeActivityUpdatesHandler();
    }

    private void requestActivityUpdatesHandler() {
        Task<Void> task = activityRecognitionClient.requestActivityUpdates(ActivityConstants.DETECTION_INTERVAL, pendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getApplicationContext(), "Successfully requested activity updates", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to request activity updates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeActivityUpdatesHandler() {
        Task<Void> task = activityRecognitionClient.removeActivityUpdates(pendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getApplicationContext(), "Successfully removed activity updates", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to remove activity updates", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
