package com.example.sosdoctors;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "FCM Service";
    private static final String CHANNEL_ID = "sosdoctors_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        // Create notification channel
        createNotificationChannel();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // Extract doctor's name from the data payload
            String doctorName = remoteMessage.getData().get("doctorName");
            if (doctorName == null) {
                doctorName = "Unknown Doctor"; // Default value if doctorName is missing
            }
            // Display the notification
            sendNotification(remoteMessage.getNotification().getBody(), doctorName);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    private void sendNotification(String messageBody, String doctorName) {
        Intent acceptIntent = new Intent(this, NotificationActionReceiver.class);
        acceptIntent.setAction("com.example.sosdoctors.ACCEPT");
        acceptIntent.putExtra("doctorName", doctorName);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Intent refuseIntent = new Intent(this, NotificationActionReceiver.class);
        refuseIntent.setAction("com.example.sosdoctors.REFUSE");
        refuseIntent.putExtra("doctorName", doctorName);
        PendingIntent refusePendingIntent = PendingIntent.getBroadcast(this, 0, refuseIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sos)
                .setContentTitle("Emergency")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .addAction(R.drawable.ic_accept, "Accept", acceptPendingIntent)
                .addAction(R.drawable.ic_refuse, "Refuse", refusePendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "SOS Doctors Channel";
        String description = "Channel for SOS Doctors notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
