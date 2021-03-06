package com.example.dronetracker2.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dronetracker2.CurrentData;
import com.example.dronetracker2.R;
import com.example.dronetracker2.ui.details.DetailItem;
import com.example.dronetracker2.ui.messages.FGObject;
import com.example.dronetracker2.ui.messages.OperationVolume;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.List;

import static java.lang.Math.atan2;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MapViewModel mapViewModel;
    private ImageButton userLocationButton;
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
        userLocationButton = myview.findViewById(R.id.userLocationImageButton);

        userLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                String providerInfo = LocationManager.NETWORK_PROVIDER;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(providerInfo);
                LatLng latlngCurrent = null;
                if (location == null)
                {
                    latlngCurrent = new LatLng(37.3352, -121.8811);
                }
                else
                {
                    latlngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                }
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngCurrent, 15));
            }
        });

        mapView = myview.findViewById(R.id.map);

        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        return myview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng marker = new LatLng(37.335153, -121.880964);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,15));
        gMap.setOnMarkerClickListener(this);
        isMapReady = true;
    }

    public void DrawFlightPlans(String gufi) {
        if (!isMapReady)
            return;

        for (OperationVolume operationVolume : CurrentData.Instance.flightplans.get(gufi).message.operation_volumes) {
            FGObject flightGeography = operationVolume.flight_geography;
            List<List<List<Double>>> coordinates = flightGeography.coordinates;
            PolygonOptions poly = new PolygonOptions();
            poly.fillColor(Color.GRAY);

            for (List<Double> coordinate : coordinates.get(0)) {
                double latitude = coordinate.get(1);
                double longitude = coordinate.get(0);
                LatLng latLng = new LatLng(latitude, longitude);
                poly.add(latLng);
            }
            gMap.addPolygon(poly);
        }
    }

    public void LockOntoAircraft(DetailItem detailItem) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(detailItem.getLatPlaceholder()),
                                   Double.parseDouble(detailItem.getLngPlaceholder())))
                .zoom(18)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void DrawAircraft(String gufi) {
        if (!isMapReady)
            return;

        List<Double> coordinate = CurrentData.Instance.aircraft.get(gufi).message.lla;
        double latitude = coordinate.get(0);
        double longitude = coordinate.get(1);
        LatLng newLatLng = new LatLng(latitude, longitude);

        if (aircraftMarkers.containsKey(gufi))
        {
            LatLng oldLatLng = aircraftMarkers.get(gufi).getPosition();
            float heading = (float)SphericalUtil.computeHeading(oldLatLng, newLatLng);
            aircraftMarkers.get(gufi).setRotation(heading);
            Log.d("WS", "" + gufi + ": old: " + oldLatLng + ", new: " + newLatLng + ", heading: " + heading);

            aircraftMarkers.get(gufi).setPosition(newLatLng);
        }
        else
        {
            String callsign = "<empty>";
            if (CurrentData.Instance.flightplans.containsKey(gufi))
            {
                callsign = CurrentData.Instance.flightplans.get(gufi).message.callsign;
            }
            BitmapDescriptor bitmapDescriptor = bitmapDescriptorFromVector(getActivity(), R.drawable.ic_flight_black_24dp);
            Marker newMarker = gMap.addMarker(new MarkerOptions().position(newLatLng).icon(bitmapDescriptor).title(callsign));
            aircraftMarkers.put(gufi, newMarker);
        }
    }

    private float degreesFromCoordinate(LatLng latlngOld, LatLng latlngNew) {

        double deltaLng = latlngNew.longitude - latlngOld.longitude;
        double lat1 = latlngOld.latitude;
        double lat2 = latlngNew.latitude;

        double angle = Math.atan2(Math.sin(deltaLng) * Math.cos(lat2),
                Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) *
                        Math.cos(lat2) * Math.cos(deltaLng));

        return (float)Math.toDegrees(angle);
    }

    public void EraseAll()
    {
        if (gMap == null)
            return;

        gMap.clear();
        aircraftMarkers.clear();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Log.d("WS", marker.getTitle());
        //marker.setTitle("David");
        //marker.showInfoWindow();
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
