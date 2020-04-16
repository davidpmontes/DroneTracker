package com.example.dronetracker2;

import android.os.Bundle;
import android.util.Log;

import com.example.dronetracker2.ui.details.DetailsFragment;
import com.example.dronetracker2.ui.home.HomeFragment;
import com.example.dronetracker2.ui.map.MapFragment;
import com.example.dronetracker2.ui.server.ServerFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CurrentData currentData;

    private ServerFragment fragmentServer;
    private MapFragment fragmentMap;
    private DetailsFragment fragmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentData = new CurrentData(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.page_viewer);
        tabLayout = findViewById(R.id.tab_layout);

        createFragments();
    }

    public void output(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentData.ProcessNewMessages(text);
            }
        });
    }

    public void MessageProcessingComplete(boolean isNewFlightPlans, boolean isNewAircraft)
    {
        if (isNewFlightPlans)
        {
            fragmentMap.DrawFlightPlans();
        }

        if (isNewAircraft)
        {
            fragmentMap.DrawAircraft();
        }
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
        fragmentServer = new ServerFragment();
        fragmentMap = new MapFragment();
        fragmentDetails = new DetailsFragment();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragmentServer, "Server");
        viewPagerAdapter.addFragment(fragmentMap, "Map");
        viewPagerAdapter.addFragment(fragmentDetails, "Details");

        viewPager.setAdapter(viewPagerAdapter);
    }

    public void OnWebSocketClose()
    {
        fragmentMap.EraseAll();
        currentData.aircraft.clear();
        currentData.flightplans.clear();
    }
}