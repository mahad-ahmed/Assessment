package com.atompunkapps.assessment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class NearbyRestaurantsActivity extends AppCompatActivity {
    RestaurantAdapter adapter;
    private ArrayList<Restaurant> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurants);

        ActionBar bar = getSupportActionBar();
        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        adapter = new RestaurantAdapter(this, list);
        ((ListView)findViewById(R.id.restaurant_list)).setAdapter(adapter);
        list.add(new Restaurant("Bazerkan Lebanese Restaurant", "Dubai", "Lebanese", 3.8f, 96, R.drawable.bazer));
        list.add(new Restaurant("Pier 7", "Dubai", "Italian", 3.6f, 183, R.drawable.pier7));
        list.add(new Restaurant("Mahad's Space Dock & Restaurant", "Orbit", "Alien", 4.8f, 1200000, R.drawable.space));
        list.add(new Restaurant("Zafran", "Dubai", "Indian", 3.6f, 301, R.drawable.zafran));
        list.add(new Restaurant("Chez Sushi - Dubai Marina", "Dubai", "Japanese", 4.2f, 1960, R.drawable.chez));
//        adapter.sortByRating(RestaurantAdapter.ASCENDING);
//        adapter.sortAlphabetically(RestaurantAdapter.DESCENDING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setBackgroundColor(Color.WHITE);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String q) {
                adapter.getFilter().filter(q);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortClicked(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        Menu menu = popup.getMenu();
        //  TODO: Create menu resource
        menu.add(0, 0, 0, "Rating(ascending)");
        menu.add(0, 1, 1, "Rating(descending)");
        menu.add(0, 2, 2, "Alphabetically(ascending)");
        menu.add(0, 3, 3, "Alphabetically(descending)");
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case 0:
                        adapter.sortByRating(RestaurantAdapter.ASCENDING);
                        break;
                    case 1:
                        adapter.sortByRating(RestaurantAdapter.DESCENDING);
                        break;
                    case 2:
                        adapter.sortAlphabetically(RestaurantAdapter.ASCENDING);
                        break;
                    case 3: adapter.sortAlphabetically(RestaurantAdapter.DESCENDING);
                }
                return true;
            }
        });
        popup.show();
    }

    public void filterClicked(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        Menu menu = popup.getMenu();
        //  TODO: Create menu resource
        menu.add(0, 0, 0, "Above 4 stars");
        menu.add(0, 1, 1, "Above 3 stars");
        menu.add(0, 2, 2, "Above 2 stars");
        menu.add(0, 3, 3, "Remove filter");
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() > 2) {
                    adapter.getRatingFilter().filter("0");
                }
                else {
                    adapter.getRatingFilter().filter(String.valueOf(4 - item.getItemId()));
                }
                return true;
            }
        });
        popup.show();
    }
}

class RestaurantAdapter extends BaseAdapter implements Filterable {
    public static final int ASCENDING = 1;
    public static final int DESCENDING = -1;
    private ArrayList<Restaurant> originalList;
    private ArrayList<Restaurant> filteredList;
    private LayoutInflater inflater;
    private SearchFilter searchFilter = new SearchFilter();
    private RatingFilter ratingFilter = new RatingFilter();

    RestaurantAdapter(Context context, ArrayList<Restaurant> lst) {
        this.originalList = lst;
        this.filteredList = lst;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.restaurant_list_item, null);

            holder = new ViewHolder();
            holder.image_view = convertView.findViewById(R.id.restaurant_photo);
            holder.name_text = convertView.findViewById(R.id.restaurant_name);
            holder.location_text = convertView.findViewById(R.id.restaurant_location);
            holder.cuisine_text = convertView.findViewById(R.id.restaurant_cuisine);
            holder.rating_num = convertView.findViewById(R.id.restaurant_rating);
            holder.rating_bar = convertView.findViewById(R.id.restaurant_rating_bar);
            holder.review_count__text = convertView.findViewById(R.id.restaurant_review_count);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        Restaurant restaurant = filteredList.get(position);
        holder.image_view.setImageResource(restaurant.imageRes);
        holder.name_text.setText(restaurant.name);
        holder.location_text.setText(restaurant.location);
        holder.cuisine_text.setText(restaurant.cuisine);
        holder.rating_num.setText(restaurant.rating.toString());
        holder.rating_bar.setRating(restaurant.rating);
        String str;
        if(restaurant.numReviews > 1000000) {
            str = String.format(Locale.getDefault(), "%.1f", (((float)restaurant.numReviews))/1000000)+"M reviews";
        }
        else if(restaurant.numReviews > 1000) {
//            float a = Math(restaurant.numReviews/1000f);
//            System.out.println(a);
            str = String.format(Locale.getDefault(), "%.1f", (((float)restaurant.numReviews))/1000)+"K reviews";
        }
        else {
            str = restaurant.numReviews+" reviews";
        }
        holder.review_count__text.setText(str);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    public Filter getRatingFilter() {
        return ratingFilter;
    }

    public void sortByRating(final int order) {
        Collections.sort(filteredList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                if(o1.rating > o2.rating) {
                    return -1*order;
                }
                else if(o1.rating < o2.rating) {
                    return order;
                }
                return 0;
            }
        });
        notifyDataSetChanged();
    }

    public void sortAlphabetically(final int order) {
        Collections.sort(filteredList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                return o2.name.compareToIgnoreCase(o1.name)*order*-1;
            }
        });
        notifyDataSetChanged();
    }

    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence input) {
            String q = input.toString().toLowerCase();
            ArrayList<Restaurant> resultsList = new ArrayList<>(originalList.size());
            for (int i=0; i<originalList.size(); i++) {
                Restaurant restaurant = originalList.get(i);
                if(restaurant.name.toLowerCase().contains(q) || restaurant.cuisine.toLowerCase().contains(q) || restaurant.location.toLowerCase().contains(q)) {
                    resultsList.add(restaurant);
                }
            }
            FilterResults results = new FilterResults();
            results.values = resultsList;
            results.count = resultsList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Restaurant>) results.values;
            notifyDataSetChanged();
        }
    }

    private class RatingFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence input) {
            // Check for invalid values
            float minRating = Float.parseFloat(input.toString());
            ArrayList<Restaurant> resultsList = new ArrayList<>(originalList.size());
            for (int i=0; i<originalList.size(); i++) {
                Restaurant restaurant = originalList.get(i);
                // Give minRating=0 to get all restaurants back
                // Check if minRating=NaN
                if(restaurant.rating >= minRating) {
                    resultsList.add(restaurant);
                }
            }
            FilterResults results = new FilterResults();
            results.values = resultsList;
            results.count = resultsList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Restaurant>) results.values;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        ImageView image_view;
        TextView name_text;
        TextView location_text;
        TextView cuisine_text;
        TextView rating_num;
        RatingBar rating_bar;
        TextView review_count__text;
    }
}

class Restaurant {
    String name;
    String location;
    String cuisine;
    Float rating;
    int numReviews;
    int imageRes;

    public Restaurant(String name, String location, String cuisine, Float rating, int numReviews, int imageRes) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.rating = rating;
        this.numReviews = numReviews;
        this.imageRes = imageRes;
        // Maybe create getter and setter for rating to avoid out of range values
    }
}