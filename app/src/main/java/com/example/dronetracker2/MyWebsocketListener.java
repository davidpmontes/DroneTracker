package com.example.dronetracker2;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebsocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private MainActivity mainActivity;

    public MyWebsocketListener (MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        mainActivity.output(text);
    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d("WS", "Error : " + t.getMessage());
    }
}
