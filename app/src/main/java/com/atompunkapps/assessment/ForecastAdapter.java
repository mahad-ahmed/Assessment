package com.atompunkapps.assessment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private ForecastData[] data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day;
        TextView tv_highest;
        TextView tv_lowest;
        ImageView iv_forecast;
        ViewHolder(View v) {
            super(v);
            tv_day = v.findViewById(R.id.forecast_day);
            tv_highest = v.findViewById(R.id.highest_temp);
            tv_lowest = v.findViewById(R.id.lowest_temp);
            iv_forecast = v.findViewById(R.id.forecast_img);
        }
    }

    ForecastAdapter(ForecastData[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_day.setText(data[position].day);
        holder.tv_highest.setText(data[position].highestTemperature+"");
        holder.tv_lowest.setText(data[position].lowestTemperature+"");
        holder.iv_forecast.setImageResource(data[position].imageResourceId);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}

class ForecastData {
    String day;
    int imageResourceId;
    int highestTemperature;
    int lowestTemperature;

    ForecastData(String day, int imageRes, int highest, int lowest) {
        this.day = day;
        this.imageResourceId = imageRes;
        this.highestTemperature = highest;
        this.lowestTemperature = lowest;
    }
}