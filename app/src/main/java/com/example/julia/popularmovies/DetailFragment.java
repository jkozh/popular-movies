/*
 * Copyright 2016.  Julia Kozhukhovskaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public class DetailFragment extends Fragment {

    public DetailFragment() {
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
            //((TextView) rootView.findViewById(R.id.detail_title))
            //        .setText(movie.title);

            // Set Movie Release date
            ((TextView) rootView.findViewById(R.id.detail_date))
                    .setText(movie.getDate(getContext()));

            // Set Movie Poster
            Picasso.with(getContext())
                    .load(movie.getPoster())
                    .into((ImageView) rootView.findViewById(R.id.detail_poster));

            // Set Movie Rating
            ((TextView) rootView.findViewById(R.id.detail_rating))
                    .setText(String.valueOf(movie.getRating()));

            // Set Movie Overview
            ((TextView) rootView.findViewById(R.id.detail_plot))
                    .setText(movie.getPlot());
        }
        return rootView;
    }
}