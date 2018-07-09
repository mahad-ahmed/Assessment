package com.atompunkapps.assessment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WeatherActivity extends AppCompatActivity {
    String arr[] = {"Dubai", "Pakistan", "United States"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ((ViewPager)findViewById(R.id.weather_pager)).setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        PagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("location", arr[position]);
            WeatherInfoFragment fragment = new WeatherInfoFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
