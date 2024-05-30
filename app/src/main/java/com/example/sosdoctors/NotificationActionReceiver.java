package com.example.sosdoctors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class NotificationActionReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationAction";
    private WebSocketClient webSocketClient;
    private static final String WEBSOCKET_SERVER_ADDRESS = "ws://localhost:8080"; // Update with ws:// for non-secure WebSocket

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String doctorName = intent.getStringExtra("doctorName");
        if ("com.example.sosdoctors.ACCEPT".equals(action)) {
            Log.d(TAG, "Intervention accepted");
            sendResponseToServer("accepted", doctorName);
        } else if ("com.example.sosdoctors.REFUSE".equals(action)) {
            Log.d(TAG, "Intervention refused");
            sendResponseToServer("refused", doctorName);
        }
    }

    private void sendResponseToServer(String response, String doctorName) {
        URI uri;
        try {
            uri = new URI(WEBSOCKET_SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            Log.e(TAG, "URI Syntax Exception: " + e.getMessage());
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d(TAG, "WebSocket connection opened");
                webSocketClient.send(doctorName + "|" + response);
                Log.d(TAG, "Response sent: " + response);
            }

            @Override
            public void onMessage(String message) {
                Log.d(TAG, "Message received: " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d(TAG, "WebSocket connection closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.e(TAG, "WebSocket error: " + ex.getMessage());
            }
        };

        webSocketClient.connect();
    }
}
