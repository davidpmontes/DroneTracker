package com.example.dronetracker2.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dronetracker2.CurrentData;
import com.example.dronetracker2.R;
import com.example.dronetracker2.ui.messages.FGObject;
import com.example.dronetracker2.ui.messages.OperationVolume;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private View myview;
    private MapView mapView;
    private GoogleMap gMap;
    private boolean isMapReady = false;
    private HashMap<String, Marker> aircraftMarkers = new HashMap<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        myview = inflater.inflate(R.layout.fragment_map, container, false);
        return myview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = myview.findViewById(R.id.map);

        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng marker = new LatLng(37.335153, -121.880964);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,15));
        isMapReady = true;
    }

    public void DrawFlightPlans() {
        if (!isMapReady)
            return;

        for (String gufi : CurrentData.Instance.flightplans.keySet()) {
            for (OperationVolume operationVolume : CurrentData.Instance.flightplans.get(gufi).message.operation_volumes)
            {
                FGObject flightGeography = operationVolume.flight_geography;
                List<List<List<Double>>> coordinates = flightGeography.coordinates;
                PolygonOptions poly = new PolygonOptions();
                poly.fillColor(Color.GRAY);

                for (List<Double> coordinate : coordinates.get(0))
                {
                    double latitude = coordinate.get(1);
                    double longitude = coordinate.get(0);
                    LatLng latLng = new LatLng(latitude, longitude);
                    poly.add(latLng);
                }
                gMap.addPolygon(poly);
            }
        }
    }

    public void DrawAircraft() {
        if (!isMapReady)
            return;

        for (String gufi : CurrentData.Instance.aircraft.keySet()) {
            BitmapDescriptor bitmapDescriptor = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_flight_black_24dp);

            List<Double> coordinate = CurrentData.Instance.aircraft.get(gufi).message.lla;
            double latitude = coordinate.get(0);
            double longitude = coordinate.get(1);
            LatLng latLng = new LatLng(latitude, longitude);

            if (aircraftMarkers.containsKey(gufi))
            {
                aircraftMarkers.get(gufi).setPosition(latLng);
            }
            else
            {
                Marker newMarker = gMap.addMarker(new MarkerOptions().position(latLng).icon(bitmapDescriptor));
                aircraftMarkers.put(gufi, newMarker);
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
