package com.atompunkapps.assessment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//  TODO: Add labels for measurement units
//  TODO: Set background color relative to current weather and time
//  TODO: Reuse data so as to not call weather API on every swipe
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
    private ImageView image;
    private RelativeLayout backgroundLayout;

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

        Bundle arguments = getArguments();
        if (arguments != null) {
            setInfo(arguments.getString("location", ""),
                    arguments.getString("day", ""),
                    arguments.getString("description", ""),
                    arguments.getInt("temperature"),
                    arguments.getInt("precipitation"),
                    arguments.getInt("humidity"),
                    arguments.getInt("wind_speed"),
                    arguments.getInt("image", R.drawable.partlycloudy),
                    arguments.getBoolean("night", false)
            );
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    private void setInfo(String location, String day, String description, int temperature, int precipitation, int humidity, int windSpeed, int imageRes, boolean night) {
        if(night) {
            backgroundLayout.setBackground(getResources().getDrawable(R.drawable.bg_night_clear));
            location_text.setTextColor(Color.WHITE);
            day_text.setTextColor(Color.WHITE);
            description_text.setTextColor(Color.WHITE);
            temperature_text.setTextColor(Color.WHITE);
            temperature_unit_label.setTextColor(Color.WHITE);
            precipitation_text.setTextColor(Color.WHITE);
            precipitation_label.setTextColor(Color.WHITE);
            precipitation_unit_label.setTextColor(Color.WHITE);
            humidity_text.setTextColor(Color.WHITE);
            humidity_label.setTextColor(Color.WHITE);
            humidity_unit_label.setTextColor(Color.WHITE);
            wind_text.setTextColor(Color.WHITE);
            wind_label.setTextColor(Color.WHITE);
            wind_unit_label.setTextColor(Color.WHITE);
        }
        else {
            backgroundLayout.setBackgroundColor(0xfffcdab0);
        }
        location_text.setText(location);
        day_text.setText(day);
        description_text.setText(description);
        temperature_text.setText(temperature+"");
        precipitation_text.setText(precipitation+"");
        humidity_text.setText(humidity+"");
        wind_text.setText(windSpeed+"");
        image.setImageResource(imageRes);
    }
}
