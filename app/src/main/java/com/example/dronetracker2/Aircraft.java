package com.example.dronetracker2;

import com.example.dronetracker2.ui.messages.MessageAolPosition;
import com.google.android.gms.maps.model.Marker;

public class Aircraft {
    public MessageAolPosition message;
    public Marker marker;

    public Aircraft(MessageAolPosition message)
    {
        this.message = message;
    }
}
