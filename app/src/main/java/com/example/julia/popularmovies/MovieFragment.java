package com.example.julia.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MovieFragment extends Fragment {

    ArrayAdapter<String> mMovieAdapter;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO: Implement actual data as thumbnails images
        String[] data = {
                "Img1",
                "Img2",
                "Img3",
                "Img4"
        };
        List<String> movies = new ArrayList<>(Arrays.asList(data));

        mMovieAdapter =
                new ArrayAdapter<>(
                        getActivity(),
                        R.layout.grid_item_movie,
                        R.id.grid_item_movie_view,
                        movies);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(mMovieAdapter);
        return rootView;
    }
}