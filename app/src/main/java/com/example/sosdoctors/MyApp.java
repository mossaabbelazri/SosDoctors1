package com.example.sosdoctors;

import android.app.Application;
import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Log;

public class MyApp extends Application {
    private static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();

        // Subscribe to the topic
        FirebaseMessaging.getInstance().subscribeToTopic("interventionResponses")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to interventionResponses topic";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d(TAG, msg);
                });
    }
}
