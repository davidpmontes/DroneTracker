package com.example.dronetracker2.ui.details;

import android.content.Context;
import android.os.Bundle;
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
import com.example.dronetracker2.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private DetailsViewModel detailsViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<DetailItem> detailItems;
    private boolean isDetailsReady;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_details, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        detailItems = new ArrayList<>();
        isDetailsReady = true;

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
        if (!isDetailsReady)
            return;

        detailItems.clear();

        for (String gufiKey : CurrentData.Instance.aircraft.keySet()) {
            Aircraft aircraft = CurrentData.Instance.aircraft.get(gufiKey);

            String gufi = aircraft.message.gufi;
            String lat = aircraft.message.lla.get(0).toString().substring(0, 10);
            String lng = aircraft.message.lla.get(1).toString().substring(0, 10);
            String callsign = "";

            if (CurrentData.Instance.flightplans.containsKey(gufiKey))
            {
                callsign = CurrentData.Instance.flightplans.get(gufiKey).message.callsign;
            }
            DetailItem detailItem = new DetailItem(callsign, "", "GUFI:", gufi, "LAT:", lat, "LNG:", lng);
            detailItems.add(detailItem);
        }

        //if (adapter == null) {
        adapter = new Adapter(detailItems, getActivity());
        recyclerView.setAdapter(adapter);
        //}
        //else
        //{
        //    adapter.notifyDataSetChanged();
        //}

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.setAdapter(adapter);
//            }
//        });
    }
}
