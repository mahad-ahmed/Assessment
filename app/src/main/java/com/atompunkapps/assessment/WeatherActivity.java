package com.atompunkapps.assessment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {
    public static final String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    static final String am_pm[] = {"AM", "PM"};
    public static HashMap<String, Integer> images = initMap();
    static String locs[] = {"Dubai,AE", "Karachi,PK", "Beijing"};
    static ForecastAdapter adapter = new ForecastAdapter(new ForecastData[0]);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        RecyclerView recyclerView = findViewById(R.id.forecast_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        final ViewPager pager = findViewById(R.id.weather_pager);
        final RequestQueue queue = Volley.newRequestQueue(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                getForecastData(locs[position], queue, images);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        WeatherInfoFragment fragments[] = new WeatherInfoFragment[locs.length];
        for(int i=0; i<fragments.length; i++) {
            fragments[i] = new WeatherInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("location", locs[i]);
            fragments[i].setArguments(bundle);
        }
        pager.setAdapter(new InfoPagerAdapter(getSupportFragmentManager(), fragments));
        if(locs.length > 0) {
            getForecastData(locs[0], queue, images);
        }
    }

    public static void getForecastData(String location, RequestQueue queue, final HashMap<String, Integer> images) {
        String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=metric&APPID=326db41cd98f57bf70a797bbee02b0de";
        JsonObjectRequest forecastRequest = new JsonObjectRequest(Request.Method.GET, forecastUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            System.out.println("==============================================");
                            JSONArray forecasts = response.getJSONArray("list");
//                            System.out.println(forecasts.getJSONObject(0));
                            ForecastData arr[] = new ForecastData[forecasts.length()];
                            for(int i=0;i<forecasts.length();i++) {
                                JSONObject obj = forecasts.getJSONObject(i);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(new Date(obj.getLong("dt")*1000));
                                String day = days[cal.get(Calendar.DAY_OF_WEEK) - 1].substring(0, 3).toUpperCase();
                                String time = cal.get(Calendar.HOUR)+am_pm[+cal.get(Calendar.AM_PM)];
                                String icon = obj.getJSONArray("weather").getJSONObject(0).getString("icon");
                                int img = R.drawable.partlycloudy;
                                if(images.containsKey(icon)) {
                                    img = images.get(icon);
                                }
                                arr[i] = new ForecastData(day,
                                        time,
                                        img,
                                        (int)Math.round(obj.getJSONObject("main").getDouble("temp_max")),
                                        (int)Math.round(obj.getJSONObject("main").getDouble("temp_min"))
                                );
                            }
                            adapter.setData(arr);
                            adapter.notifyDataSetChanged();
//                            System.out.println("======================END=====================");
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("###############################################");
//                        System.out.println(error.toString());
//                        System.out.println("###############################################");
                    }
                });
        forecastRequest.setRetryPolicy(new DefaultRetryPolicy(4000, 1, 0));
        queue.add(forecastRequest);
    }

    private static HashMap<String, Integer> initMap() {
        HashMap<String, Integer> images = new HashMap<>();
        images.put("01d", R.drawable.clear);  // Clear sky
        images.put("02d", R.drawable.partlycloudy); // Few clouds
        images.put("03d", R.drawable.cloudy); // Scattered clouds
        images.put("04d", R.drawable.partlysunny); // Broken clouds
        images.put("09d", R.drawable.rain); // Shower rain
        images.put("10d", R.drawable.rain); // Rain
        images.put("11d", R.drawable.tstorms); // Thunderstorm
        images.put("13d", R.drawable.snow); // Snow
        images.put("50d", R.drawable.fog); // Mist

        images.put("01n", R.drawable.nt_clear);  // Clear sky
        images.put("02n", R.drawable.nt_partlycloudy); // Few clouds
        images.put("03n", R.drawable.nt_cloudy); // Scattered clouds
        images.put("04n", R.drawable.nt_mostlycloudy); // Broken clouds
        images.put("09n", R.drawable.rain); // Shower rain
        images.put("10n", R.drawable.rain); // Rain
        images.put("11n", R.drawable.tstorms); // Thunderstorm
        images.put("13n", R.drawable.snow); // Snow
        images.put("50n", R.drawable.fog); // Mist

        return images;
    }

    private class InfoPagerAdapter extends FragmentStatePagerAdapter {
        WeatherInfoFragment fragments[];
        InfoPagerAdapter(FragmentManager manager, WeatherInfoFragment fragments[]) {
            super(manager);
            this.fragments = fragments;
        }



        @Override
        public Fragment getItem(int position) {
            //  TODO: Reuse data
//            System.out.println("getItem("+position+")");
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}

