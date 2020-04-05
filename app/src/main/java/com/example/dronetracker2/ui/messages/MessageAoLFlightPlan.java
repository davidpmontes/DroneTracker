package com.example.dronetracker2.ui.messages;

import java.util.List;

public class MessageAoLFlightPlan{
    public String callsign;
    public String gufi;
    public String state;
    public List<OperationVolume> operation_volumes;
    public ControllerLocation controller_location;
    public GCSLocation gcs_location;
    public MetaData metaData;
    public List<Double> lla;
}
