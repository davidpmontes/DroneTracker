package com.example.dronetracker2.ui.messages;

public class OperationVolume{
    public int ordinal;
    public boolean near_structure;
    public String effective_time_begin;
    public String effective_time_end;
    public AltitudeObj min_altitude;
    public AltitudeObj max_altitude;
    public boolean beyond_visual_line_of_sight;
    public String volume_type;
    public FGObject flight_geography;
}
