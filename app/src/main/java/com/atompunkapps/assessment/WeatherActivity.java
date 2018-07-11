package com.atompunkapps.assessment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class WeatherActivity extends AppCompatActivity {
    String arr[] = {"Dubai", "Pakistan", "United States"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ((ViewPager)findViewById(R.id.weather_pager)).setAdapter(new PagerAdapter(getSupportFragmentManager()));

        RecyclerView recyclerView = findViewById(R.id.forecast_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ForecastData arr[] = {
                new ForecastData("MON", R.drawable.partly_cloudy, 37, 27),
                new ForecastData("TUE", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("WED", R.drawable.partly_cloudy, 37, 27),
                new ForecastData("THU", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("FRI", R.drawable.partly_cloudy, 37, 27),
                new ForecastData("SAT", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("SUN", R.drawable.partly_cloudy, 37, 27),
                new ForecastData("MON", R.drawable.nt_mostlycloudy, 37, 27)
        };
        recyclerView.setAdapter(new ForecastAdapter(arr));
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

