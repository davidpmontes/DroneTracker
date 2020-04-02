package com.example.dronetracker2;

import android.os.Bundle;
import android.util.Log;

import com.example.dronetracker2.ui.details.DetailsFragment;
import com.example.dronetracker2.ui.home.HomeFragment;
import com.example.dronetracker2.ui.map.MapFragment;
import com.example.dronetracker2.ui.server.ServerFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private HomeFragment fragmentHome;
    private ServerFragment fragmentServer;
    private MapFragment fragmentMap;
    private DetailsFragment fragmentDetails;
    private HashMap<String, ArrayList<ArrayList<LatLng>>> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.page_viewer);
        tabLayout = findViewById(R.id.tab_layout);

        createFragments();
        //fragmentMap.DoSomething();

        fetchJson();


    }

    private void fetchJson(){
        OkHttpClient client = new OkHttpClient();
        String url ="http://10.0.2.2:3000/";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("fail", "fail");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String body = response.body().string();
                    //Log.i("myResponse", body);

                    Gson gson = new GsonBuilder().create();

                    DroneData[] mcArray = gson.fromJson(body, DroneData[].class);
                    List<DroneData> datafeed = Arrays.asList(mcArray);

                    //HashMap<String, ArrayList<ArrayList<LatLng>>> hashMap = new HashMap<>();

                    int datafeedSize = datafeed.size()-1;
                    int operationVolumesSize = 0;
                    int coordinatesArray = 0;


                    for(int datafeedIndex = 0; datafeedIndex<=datafeedSize; datafeedIndex++){
                        ArrayList<ArrayList<LatLng>> ListFlightGeographyPolygon = new ArrayList<ArrayList<LatLng>>();
                        String gufi = datafeed.get(datafeedIndex).MessageAolFlightPlan.gufi;
                        operationVolumesSize = datafeed.get(datafeedIndex).MessageAolFlightPlan.operation_volumes.size()-1;
                        for(int operationVolumesIndex = 0; operationVolumesIndex<=operationVolumesSize; operationVolumesIndex++){
                            coordinatesArray = datafeed.get(datafeedIndex).MessageAolFlightPlan.operation_volumes.
                                    get(operationVolumesIndex).flight_geography.coordinates.get(0).size()-1;
                            ArrayList<LatLng> flightGeographyPolygon = new ArrayList<>();
                            for(int coordinatesArrayIndex=0; coordinatesArrayIndex<=coordinatesArray; coordinatesArrayIndex++){
                                List<Double> coordinates = datafeed.get(datafeedIndex).MessageAolFlightPlan.
                                        operation_volumes.get(operationVolumesIndex)
                                        .flight_geography.coordinates.get(0).get(coordinatesArrayIndex);

                                Double lat = coordinates.get(1);
                                Double lng = coordinates.get(0);
                                LatLng latLng = new LatLng(lat,lng);
                                flightGeographyPolygon.add(latLng);
                            }
                            ListFlightGeographyPolygon.add(flightGeographyPolygon);
                        }
                        hashMap.put(gufi,ListFlightGeographyPolygon);
                    }

                    /*for (String gufi : hashMap.keySet()) {
                        //Log.i("hashMap", "Element at key $objectName : ${hashMap[objectName]}");
                        Log.i("hashMap",gufi.toString() + " " + hashMap.get(gufi).toString());
                    }*/



                    /*int value = fragmentMap.DoSomething(hashMap);
                    while(value == -1){
                        try {
                            Thread.sleep(1000);
                            value = fragmentMap.DoSomething(hashMap);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            }
        });

    }

    public void isMapReady(){
        fragmentMap.DoSomething(hashMap);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    private void createFragments() {
        fragmentHome = new HomeFragment();
        fragmentServer = new ServerFragment();
        fragmentMap = new MapFragment();
        fragmentDetails = new DetailsFragment();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentHome, "Home");
        viewPagerAdapter.addFragment(fragmentServer, "Server");
        viewPagerAdapter.addFragment(fragmentMap, "Map");
        viewPagerAdapter.addFragment(fragmentDetails, "Details");

        viewPager.setAdapter(viewPagerAdapter);
    }
}

class DroneData{
    MessageAOLFlightPlan MessageAolFlightPlan;
}

class MessageAOLFlightPlan{
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

//class DroneData(val MessageAolFlightPlan: MessageAOLFlightPlan)

//class MessageAOLFlightPlan(val callsign: String, val gufi: String, val state: String, val operation_volumes: List<OperationVolume>, val controller_location: ControllerLocation, val gcs_location: GCSLocation, val metadata: MetaData, val lla: List<Float>)

//class OperationVolume(val ordinal: Int, val near_structure: Boolean, val effective_time_begin: String, effective_time_end: String, val min_altitude: AltitudeObj, val max_altitude: AltitudeObj, val beyond_visual_line_of_sight: Boolean, val volume_type: String, val flight_geography: FGObject)

//class AltitudeObj(val altitude_value: Int, val vertical_reference: String, val units_of_measure: String, val source: String)

//class FGObject(val type: String, val coordinates: List<List<List<Double>>>)

//class ControllerLocation(val coordinates: List<Float>, type: String)

//class GCSLocation(val coordinates: List<Float>, type: String)

//class MetaData(val data_collection: Boolean, val scenario: String, val test_card: String, val call_sign: String, val test_type: String, val source: String, val event_id: String, val location: String, val setting: String, val free_text: String, val modified: Boolean, val test_run: String)
