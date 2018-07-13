package com.atompunkapps.assessment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import static com.atompunkapps.assessment.WeatherActivity.days;

//  TODO: Set background color relative to current weather and time
//  TODO: Set placeholders for data

public class WeatherInfoFragment extends Fragment {
    private TextView location_text;
    private TextView day_text;
    private TextView description_text;
    private TextView temperature_text;
    private TextView temperature_unit_label;
    private TextView precipitation_text;
    private TextView precipitation_unit_label;
    private TextView precipitation_label;
    private TextView humidity_text;
    private TextView humidity_unit_label;
    private TextView humidity_label;
    private TextView wind_text;
    private TextView wind_unit_label;
    private TextView wind_label;
    private TextView no_data;
    private ImageView image;
    private RelativeLayout backgroundLayout;
    private ProgressBar spinner;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        location_text = view.findViewById(R.id.weather_info_location);
        day_text = view.findViewById(R.id.weather_info_day);
        description_text = view.findViewById(R.id.weather_info_description);
        temperature_text = view.findViewById(R.id.weather_info_temperature);
        temperature_unit_label = view.findViewById(R.id.temp_unit_label);
        precipitation_text = view.findViewById(R.id.weather_info_precipitation);
        precipitation_label = view.findViewById(R.id.prec_label);
        precipitation_unit_label = view.findViewById(R.id.prec_unit_label);
        humidity_text = view.findViewById(R.id.weather_info_humidity);
        humidity_label = view.findViewById(R.id.humidity_label);
        humidity_unit_label = view.findViewById(R.id.humidity_unit_label);
        wind_text = view.findViewById(R.id.weather_info_wind);
        wind_label = view.findViewById(R.id.wind_label);
        wind_unit_label = view.findViewById(R.id.wind_unit_label);
        image = view.findViewById(R.id.weather_info_image);
        backgroundLayout = view.findViewById(R.id.fragment_background);
        spinner = view.findViewById(R.id.loading_spinner);
        swipeRefresh = view.findViewById(R.id.weather_refresh);
        no_data = view.findViewById(R.id.current_no_data);

        view.findViewById(R.id.restaurants_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  TODO: Send location to activity
                startActivity(new Intent(getContext(), NearbyRestaurantsActivity.class));
            }
        });

        spinner.setVisibility(View.VISIBLE);
        Bundle arguments = getArguments();
        if (arguments != null) {
            final String location = arguments.getString("location", "Dubai,AE");
            getCurrentWeatherData(location, Volley.newRequestQueue(getContext()), WeatherActivity.images);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    getCurrentWeatherData(location, queue, WeatherActivity.images);
                    WeatherActivity.getForecastData(location, queue, WeatherActivity.images);
                }
            });
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    private void getCurrentWeatherData(String location, RequestQueue queue, final HashMap<String, Integer> iconSet) {
        //  Should probably hide the API key!
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&APPID=326db41cd98f57bf70a797bbee02b0de";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(response.getLong("dt") * 1000));
                            int hourRecorded = cal.get(Calendar.HOUR_OF_DAY);

                            JSONObject sys = response.getJSONObject("sys");
                            cal.setTime(new Date(sys.getLong("sunrise") * 1000));
                            int sunrise = cal.get(Calendar.HOUR_OF_DAY);

                            cal.setTime(new Date(sys.getLong("sunset") * 1000));
                            int sunset = cal.get(Calendar.HOUR_OF_DAY);
                            setMode(hourRecorded >= sunset || hourRecorded < sunrise);

                            day_text.setText(days[cal.get(Calendar.DAY_OF_WEEK) - 1]);

                            location_text.setText(response.getString("name") + ", " + sys.getString("country"));

                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            String description = weather.getString("description");
                            description_text.setText(description.substring(0, 1).toUpperCase() + description.substring(1));

                            String icon = weather.getString("icon");
                            if (iconSet.containsKey(icon)) {
                                image.setImageResource(iconSet.get(icon));
                            }
                            JSONObject main = response.getJSONObject("main");
                            temperature_text.setText(((int) Math.round(main.getDouble("temp"))) + "");
                            humidity_text.setText(((int) Math.round(main.getDouble("humidity"))) + "");
                            wind_text.setText(((int) Math.round(response.getJSONObject("wind").getDouble("speed"))) + "");

                            //  TODO: Get precipitation from API
                            precipitation_text.setText(((int)(Math.random() * 65))+"");
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        spinner.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        no_data.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        if(temperature_text.getText().length() == 0) {
                            no_data.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
//                        System.out.println("###############################################");
//                        System.out.println(error.networkResponse);
//                        System.out.println("###############################################");
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(4000, 1, 0));
        queue.add(request);
    }

    private void setMode(boolean night) {
        int defaultColor;
        int lighterDark;
        int color;
        if(night) {
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.bg_night_clear));
            defaultColor = Color.WHITE;
            lighterDark = Color.WHITE;
            color = Color.WHITE;
        }
        else {
//            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.bg_day_clear));
            backgroundLayout.setBackgroundColor(0xfffcdab0);
            defaultColor = Color.parseColor("#8a000000");
            lighterDark = Color.parseColor("#111111");
            color = Color.BLACK;
        }
        day_text.setTextColor(defaultColor);
        description_text.setTextColor(defaultColor);
        location_text.setTextColor(color);
        temperature_text.setTextColor(color);
        temperature_unit_label.setTextColor(color);
        precipitation_text.setTextColor(lighterDark);
        precipitation_unit_label.setTextColor(lighterDark);
        precipitation_label.setTextColor(defaultColor);
        humidity_text.setTextColor(lighterDark);
        humidity_unit_label.setTextColor(lighterDark);
        humidity_label.setTextColor(defaultColor);
        wind_text.setTextColor(lighterDark);
        wind_unit_label.setTextColor(lighterDark);
        wind_label.setTextColor(defaultColor);
    }
}
