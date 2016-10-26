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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private MovieGridAdapter mMovieGridAdapter;
    private ArrayList<Movie> mMovies;
    private String mSortBy = Config.POPULARITY_DESC;

    // newInstance constructor for creating fragment with arguments
    public static MovieGridFragment newInstance(String sortBy) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle args = new Bundle();
        args.putString(Config.SORT_SETTING_KEY, sortBy);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieGridFragment() {
        // Required empty public constructor
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (getArguments()!=null) {
            mSortBy = getArguments().getString(Config.SORT_SETTING_KEY);
            Log.e(LOG_TAG, "SORT_BY="+mSortBy);
            updateMovies(mSortBy);
       } else {
           Log.e(LOG_TAG, "NOTHING WAS PASSED!");
       }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieGridAdapter = new MovieGridAdapter(getContext(), new ArrayList<Movie>());
        RecyclerView mGridRecyclerView = (RecyclerView) view.findViewById(R.id.gridview_movies);
        int gridColsNumber = getResources().getInteger(R.integer.grid_number_cols);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridColsNumber = getResources().getInteger(R.integer.grid_number_cols_portrait);
        }
        mGridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridColsNumber));
        mGridRecyclerView.setAdapter(mMovieGridAdapter);
        return view;
    }

    public void updateMovies(String sortBy) {
        // TODO: change
        if (mSortBy.equals(Config.FAVORITES)) {
            new FetchFavoritesTask(getActivity(), mMovieGridAdapter, mMovies).execute();
        } else {
            new FetchMoviesTask(mMovieGridAdapter).execute(sortBy);
        }
    }

//    @Override
//    public void onResume() {  // when press back button a fragment need updates
//        super.onResume();
//        updateMovies(mSortBy);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovies != null) {
            // Save state of activity as parcelable
            outState.putParcelableArrayList(Config.MOVIES_KEY, mMovies);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Config.MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(Config.MOVIES_KEY);
                mMovieGridAdapter.setData(mMovies);
            } else {
                updateMovies(mSortBy);
            }
        } else {
            updateMovies(mSortBy);
        }
    }
}