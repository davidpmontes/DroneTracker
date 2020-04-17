package com.example.dronetracker2.ui.details;

public class DetailItem {
    private String callsign;
    private String callsignPlaceholder;
    private String gufi;
    private String gufiPlaceHolder;
    private String lat;
    private String latPlaceholder;
    private String lng;
    private String lngPlaceholder;

    public DetailItem(String callsign, String callsignPlaceholder, String gufi, String gufiPlaceHolder, String lat, String latPlaceholder, String lng, String lngPlaceholder) {
        this.callsign = callsign;
        this.callsignPlaceholder = callsignPlaceholder;
        this.gufi = gufi;
        this.gufiPlaceHolder = gufiPlaceHolder;
        this.lat = lat;
        this.latPlaceholder = latPlaceholder;
        this.lng = lng;
        this.lngPlaceholder = lngPlaceholder;
    }

    public String getCallsign() {
        return callsign;
    }

    public String getCallsignPlaceholder() {
        return callsignPlaceholder;
    }

    public String getGufi() {
        return gufi;
    }

    public String getGufiPlaceHolder() {
        return gufiPlaceHolder;
    }

    public String getLat() {
        return lat;
    }

    public String getLatPlaceholder() {
        return latPlaceholder;
    }

    public String getLng() {
        return lng;
    }

    public String getLngPlaceholder() {
        return lngPlaceholder;
    }
}
