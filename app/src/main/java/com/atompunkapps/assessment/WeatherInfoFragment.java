package com.atompunkapps.assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView precipitation_text;
    private TextView humidity_text;
    private TextView wind_speed_text;
    private ImageView image;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        location_text = view.findViewById(R.id.weather_info_location);
        day_text = view.findViewById(R.id.weather_info_day);
        description_text = view.findViewById(R.id.weather_info_description);
        temperature_text = view.findViewById(R.id.weather_info_temperature);
        precipitation_text = view.findViewById(R.id.weather_info_precipitation);
        humidity_text = view.findViewById(R.id.weather_info_humidity);
        wind_speed_text = view.findViewById(R.id.weather_info_wind);
        image = view.findViewById(R.id.weather_info_image);

        view.findViewById(R.id.fragment_background).setBackgroundColor(0xfffcdab0);

        Bundle arguments = getArguments();
        if (arguments != null) {
            setInfo(arguments.getString("location", ""),
                    arguments.getString("day", ""),
                    arguments.getString("description", ""),
                    arguments.getInt("temperature"),
                    arguments.getInt("precipitation"),
                    arguments.getInt("humidity"),
                    arguments.getInt("wind_speed"),
                    arguments.getInt("image", R.drawable.partlycloudy));
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    private void setInfo(String location, String day, String description, int temperature, int precipitation, int humidity, int windSpeed, int imageRes) {
        location_text.setText(location);
        day_text.setText(day);
        description_text.setText(description);
        temperature_text.setText(temperature+"");
        precipitation_text.setText(precipitation+"");
        humidity_text.setText(humidity+"");
        wind_speed_text.setText(windSpeed+"");
        image.setImageResource(imageRes);
    }
}
