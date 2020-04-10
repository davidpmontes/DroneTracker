package com.example.dronetracker2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class CurrentData {
    public static CurrentData Instance;

    public HashMap<String, FlightPlan> flightplans = new HashMap<>();
    public HashMap<String, Aircraft> aircraft = new HashMap<>();
    private MainActivity mainActivity;

    public CurrentData(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        Instance = this;
    }

    public void ProcessNewMessages(String rawMessage)
    {
        Gson gson = new GsonBuilder().create();
        MessageWrapperOperation messageWrapperAolFlightPlan = new MessageWrapperOperation();
        MessageWrapperPosition messageWrapperAolPosition = new MessageWrapperPosition();

        boolean isNewFlightPlans = false;
        boolean isNewAircraft = false;

        try {
            messageWrapperAolFlightPlan = gson.fromJson(rawMessage, MessageWrapperOperation.class);
            String gufi = messageWrapperAolFlightPlan.MessageAolFlightPlan.gufi;
            isNewFlightPlans = true;
            if (flightplans.containsKey(gufi))
            {
                FlightPlan oldFlightPlan = flightplans.get(gufi);
                oldFlightPlan.message = messageWrapperAolFlightPlan.MessageAolFlightPlan;
            }
            else
            {
                FlightPlan newFlightPlan = new FlightPlan(messageWrapperAolFlightPlan.MessageAolFlightPlan);
                flightplans.put(gufi, newFlightPlan);
            }
        } catch (Exception e){
        }

        try {
            messageWrapperAolPosition = gson.fromJson(rawMessage, MessageWrapperPosition.class);
            String gufi = messageWrapperAolPosition.MessageAolPosition.gufi;
            isNewAircraft = true;
            if (aircraft.containsKey(gufi))
            {
                Aircraft oldAircraft = aircraft.get(gufi);
                oldAircraft.message = messageWrapperAolPosition.MessageAolPosition;
            }
            else
            {
                Aircraft newAircraft = new Aircraft(messageWrapperAolPosition.MessageAolPosition);
                aircraft.put(gufi, newAircraft);
            }
        } catch (Exception e) {
        }

        mainActivity.MessageProcessingComplete(isNewFlightPlans, isNewAircraft);
    }
}
