package com.example.sosdoctors;





import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;







public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String CHANNEL_ID = "default_channel_id";

    FirebaseAuth auth;
    Button button;
    TextView textView; // TextView for app name or other uses

    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        // Subscribe to the topic
        FirebaseMessaging.getInstance().subscribeToTopic("interventionResponses")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to interventionResponses topic";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d(TAG, msg);
                });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    // Get the FCM token
                    String token = task.getResult();
                    // Log and toast
                    Log.d("FCM Token", token);
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    // Save this token to the database associated with the doctor
                });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //firestore




        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.appName); // Initialized TextView

        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), com.example.sosdoctors.LoginActivity.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), com.example.sosdoctors.LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void createNotificationChannel() {
        CharSequence name = "Default Channel";
        String description = "Channel for default notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}

