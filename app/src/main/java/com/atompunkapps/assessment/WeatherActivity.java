package com.atompunkapps.assessment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {
    static final String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final HashMap<String, Integer> images = new HashMap<>();
        initMap(images);

        String url = "http://api.openweathermap.org/data/2.5/weather?q=Karachi,PK&units=metric&APPID=326db41cd98f57bf70a797bbee02b0de";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Bundle bundle = new Bundle();
                        try {
//                            System.out.println("==============================================");
//                            System.out.println(response);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(response.getLong("dt")*1000));
                            bundle.putString("day", days[cal.get(Calendar.DAY_OF_WEEK) - 1]);
                            System.out.println();
//                            System.out.println(response.getString("name"));
                            bundle.putString("location", response.getString("name")+", Pakistan");
//                            System.out.println(response.getJSONObject("sys").getString("country"));
//                            System.out.println(response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            String description = weather.getString("description");
                            description = description.substring(0, 1).toUpperCase()+description.substring(1);
                            bundle.putString("description", description);
                            String icon = weather.getString("icon");
                            if(images.containsKey(icon)) {
                                bundle.putInt("image", images.get(icon));
                            }
                            JSONObject main = response.getJSONObject("main");
//                            System.out.println(main.getDouble("temp"));
                            bundle.putInt("temperature", (int)Math.round(main.getDouble("temp")));
//                            System.out.println(main.getDouble("temp_min"));
//                            System.out.println(main.getDouble("temp_max"));
//                            System.out.println(main.getDouble("humidity"));
                            bundle.putInt("humidity", (int)Math.round(main.getDouble("humidity")));
//                            System.out.println(response.getJSONObject("wind").getDouble("speed"));
                            bundle.putInt("wind_speed", (int)Math.round(response.getJSONObject("wind").getDouble("speed")));
//                            System.out.println("======================END=====================");
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                        }
                        ((ViewPager)findViewById(R.id.weather_pager)).setAdapter(new InfoPagerAdapter(getSupportFragmentManager(), bundle));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("###############################################");
                        System.out.println(error.networkResponse);
                        System.out.println("###############################################");
                    }
                });

        String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Karachi,PK&APPID=326db41cd98f57bf70a797bbee02b0de";
        JsonObjectRequest forecastRequest = new JsonObjectRequest(Request.Method.GET, forecastUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("==============================================");
                            System.out.println(response);
                            System.out.println("======================END=====================");
                        }
                        catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("###############################################");
                        System.out.println(error.networkResponse);
                        System.out.println("###############################################");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.add(forecastRequest);

        RecyclerView recyclerView = findViewById(R.id.forecast_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ForecastData arr[] = {
                new ForecastData("MON", R.drawable.partlycloudy, 37, 27),
                new ForecastData("TUE", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("WED", R.drawable.partlycloudy, 37, 27),
                new ForecastData("THU", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("FRI", R.drawable.partlycloudy, 37, 27),
                new ForecastData("SAT", R.drawable.nt_mostlycloudy, 37, 27),
                new ForecastData("SUN", R.drawable.partlycloudy, 37, 27),
                new ForecastData("MON", R.drawable.nt_mostlycloudy, 37, 27)
        };
        recyclerView.setAdapter(new ForecastAdapter(arr));
    }

    private void initMap(HashMap<String, Integer> images) {
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
    }

    private class InfoPagerAdapter extends FragmentStatePagerAdapter {
        Bundle bundle;
        InfoPagerAdapter(FragmentManager manager, Bundle b) {
            super(manager);
            this.bundle = b;
        }

        @Override
        public Fragment getItem(int position) {
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

