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

import com.example.dronetracker2.Aircraft;
import com.example.dronetracker2.CurrentData;
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

        //parseJSON();

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

    public void UpdateDetails() {
        detailItems.clear();

        for (String gufiKey : CurrentData.Instance.aircraft.keySet()) {
            Aircraft aircraft = CurrentData.Instance.aircraft.get(gufiKey);

            String gufi = aircraft.message.gufi;
            String lat = aircraft.message.lla.get(1).toString();
            String lng = aircraft.message.lla.get(0).toString();
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
}
