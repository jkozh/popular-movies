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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.julia.popularmovies.details.FetchFavoritesTask;
import com.example.julia.popularmovies.models.Movie;

import java.util.ArrayList;

public class MovieGridFragment extends Fragment {

    private final String LOG_TAG = MovieGridFragment.class.getSimpleName();


    private GridView mGridView;
    private MovieGridAdapter mMovieGridAdapter;
    private ArrayList<Movie> mMovies = null;
    private String mSortBy = Config.POPULARITY_DESC;
    private Spinner spinner;
    private Bundle myBundle;
    private static final String MOVIES_KEY = "movies";

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview_movies);
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());
        mGridView.setAdapter(mMovieGridAdapter);
//        Log.e(LOG_TAG,"onCreateView");
//
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e(LOG_TAG,"CLICK!!!!!!!!!!!");
//                Movie movie = mMovieGridAdapter.getItem(i);
//                ((Callback) getActivity()).onItemSelected(movie);
//            }
//        });
        return view;
    }

    private void updateMovies(String sortBy) {
        if (mSortBy.equals(Config.FAVORITE)) {
            new FetchFavoritesTask(getActivity(), mMovieGridAdapter, mMovies).execute();
        } else {
            new FetchMoviesTask(mMovieGridAdapter).execute(sortBy);
        }
    }

    @Override
    public void onResume() {  // when press back button a fragment need updates
        super.onResume();
        updateMovies(mSortBy);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner_sortby);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.sortby_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Reduce some space between text and an expand arrow icon
        spinner.setGravity(Gravity.END);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // popular
                        mSortBy = Config.POPULARITY_DESC;
                        break;
                    case 1:
                        // top rated
                        mSortBy = Config.RATING_DESC;
                        break;
                    case 2:
                        // favorite
                        mSortBy = Config.FAVORITE;
                        break;
                    default:
                        Log.e(LOG_TAG, "Something went wrong with spinner");
                }
                updateMovies(mSortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (myBundle != null) {
            // Retrieve saved selection of spinner
            spinner.setSelection(myBundle.getInt(Config.SPINNER_KEY, 0));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies(mSortBy);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!mSortBy.contentEquals(Config.POPULARITY_DESC)) {
            outState.putString(Config.SORT_SETTING_KEY, mSortBy);
        }
        if (mMovies != null) {
            // Save state of activity as parcelable
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        // Save selection for spinner
        outState.putInt(Config.SPINNER_KEY, spinner.getSelectedItemPosition());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Config.SORT_SETTING_KEY)) {
                mSortBy = savedInstanceState.getString(Config.SORT_SETTING_KEY);
            }
            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mMovieGridAdapter.setData(mMovies);
            } else {
                updateMovies(mSortBy);
            }
            this.myBundle = savedInstanceState;
        } else {
            updateMovies(mSortBy);
        }
    }
}