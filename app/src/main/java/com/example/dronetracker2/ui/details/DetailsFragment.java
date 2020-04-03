package com.example.dronetracker2.ui.details;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dronetracker2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsFragment extends Fragment {

    private DetailsViewModel detailsViewModel;

    private RecyclerView rv;
    private RecyclerView.Adapter adapter;

    private List<DetailItem> detailItems;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailsViewModel =
                ViewModelProviders.of(this).get(DetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        rv = root.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        detailItems = new ArrayList<>();

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                parseJSON();
//            }
//        });

        parseJSON();

        return root;
    }

    private void parseJSON() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("FAIL", "Failed to retreive data");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();

                Gson gson = new GsonBuilder().create();

                DroneData[] data = gson.fromJson(body, DroneData[].class);
                List<DroneData> datafeed = Arrays.asList(data);

                for (int i = 0; i < datafeed.size(); i++) {
                    String gufi = datafeed.get(i).MessageAolFlightPlan.gufi;
                    String lat = datafeed.get(i).MessageAolFlightPlan.gcs_location.coordinates.get(1).toString();
                    String lng = datafeed.get(i).MessageAolFlightPlan.gcs_location.coordinates.get(0).toString();
                    DetailItem detailItem = new DetailItem("GUFI: ", gufi, "LAT: ", lat, "LNG: ", lng);
                    detailItems.add(detailItem);
                }
                adapter = new Adapter(detailItems, getActivity());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setAdapter(adapter);
                    }
                });

            }
        });
    }

    class DroneData{
        MessageAolPosition MessageAolPosition;
        MessageAoLFlightPlan MessageAolFlightPlan;
    }

    class MessageAolPosition{
        String callsign;
        String gufi;
        List<Double> hpr;
        List<Double> lla;
        Double groundSpeed;
        String misc;
        String time;
        String time_measured;
        String time_sent;
        String uss_name;
    }

    class MessageAoLFlightPlan{
        String callsign;
        String gufi;
        String state;
        List<OperationVolume> operation_volumes;
        ControllerLocation controller_location;
        GCSLocation gcs_location;
        MetaData metaData;
        List<Double> lla;
    }

    class OperationVolume{
        int ordinal;
        boolean near_structure;
        String effective_time_begin;
        String effective_time_end;
        AltitudeObj min_altitude;
        AltitudeObj max_altitude;
        boolean beyond_visual_line_of_sight;
        String volume_type;
        FGObject flight_geography;
    }

    class AltitudeObj{
        int altitude_value;
        String vertical_reference;
        String units_of_measure;
        String source;
    }

    class FGObject{
        String type;
        List<List<List<Double>>> coordinates;
    }

    class ControllerLocation{
        List<Double> coordinates;
        String type;
    }

    class GCSLocation{
        List<Double> coordinates;
        String type;
    }

    class MetaData{
        boolean data_collection;
        String scenario;
        String test_card;
        String call_sign;
        String test_type;
        String source;
        String event_id;
        String location;
        String setting;
        String free_text;
        boolean modified;
        String test_run;
    }

}
