package com.example.dronetracker2.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dronetracker2.ui.messages.*;
import com.example.dronetracker2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

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

        View root = inflater.inflate(R.layout.fragment_details, container, false);
        rv = root.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        detailItems = new ArrayList<>();

        parseJSON();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

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
}
