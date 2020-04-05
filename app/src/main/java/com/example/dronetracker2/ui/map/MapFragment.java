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

import com.example.dronetracker2.MainActivity;
import com.example.dronetracker2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private View myview;
    private MapView mapView;
    private GoogleMap gMap;

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
        LatLng marker = new LatLng(39.52721586111111, -119.81009614166666);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,15));
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.isMapReady();
    }

    public void DrawFlightPlans(HashMap<String, ArrayList<ArrayList<LatLng>>> hashMap) {
        gMap.clear();
        //Log.i("doSomething", "I am a map");
        for (String gufi : hashMap.keySet()) {
            Log.i("hashMap",gufi + " " + hashMap.get(gufi).toString());
            ArrayList<ArrayList<LatLng>> LFGP = hashMap.get(gufi);
            //Log.i("LFGP",LFGP.toString());
            for(int LFGPIndex = 0; LFGPIndex<LFGP.size();  LFGPIndex++){
                Log.i("LFGP",LFGP.get(LFGPIndex).toString());
                ArrayList<LatLng> fGP = LFGP.get(LFGPIndex);
                PolygonOptions poly = new PolygonOptions();
                poly.fillColor(Color.GRAY);
                for(int fGPIndex = 0; fGPIndex<fGP.size(); fGPIndex++){
                    Log.i("fGP",fGP.get(fGPIndex).toString());
                    Double lat = fGP.get(fGPIndex).latitude;
                    Double lng = fGP.get(fGPIndex).longitude;
                    LatLng latLng = new LatLng(lat,lng);
                    poly.add(latLng);
                    Log.i("latlng", lat.toString() + " " + lng.toString());
                }
                gMap.addPolygon(poly);
            }
        }
    }

    public void DrawAircraft(ArrayList<LatLng> aircraftPosition){
        for (LatLng latLng : aircraftPosition) {
            BitmapDescriptor bitmapDescriptor = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_flight_black_24dp);
            gMap.addMarker(new MarkerOptions().position(latLng).icon(bitmapDescriptor));
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
