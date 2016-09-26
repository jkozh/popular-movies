package com.example.julia.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            // Set Movie Title
            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(movie.title);

            // Set Movie Release date
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(movie.releaseDate);

            // Set Movie Poster
            Picasso.with(getContext())
                    .load(movie.posterUrl)
                    .into((ImageView) rootView.findViewById(R.id.detail_poster));

            // Set Movie Vote Average
            ((TextView) rootView.findViewById(R.id.detail_vote_average))
                    .setText(movie.voteAverage);

            // Set Movie Overview
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(movie.overview);
        }
        return rootView;
    }
}