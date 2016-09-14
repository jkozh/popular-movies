package com.example.julia.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private String[] mMovieStr;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            mMovieStr = intent.getStringArrayExtra(Intent.EXTRA_TEXT);

            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(mMovieStr[0]);
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(mMovieStr[1]);
            ((TextView) rootView.findViewById(R.id.detail_poster))
                    .setText(mMovieStr[2]);
            ((TextView) rootView.findViewById(R.id.detail_vote_average))
                    .setText(mMovieStr[3]);
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(mMovieStr[4]);
        }

        return rootView;
    }

}
