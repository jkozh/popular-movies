package com.example.julia.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context context;

    public MovieAdapter(Activity context, List<Movie> movies){
        super(context, 0, movies);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.grid_item_movie_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Movie movie = getItem(position);

        Picasso.with(context).load(movie.posterUrl).into(holder.image);
        return view;
    }


    static class ViewHolder {
        ImageView image;
    }
}