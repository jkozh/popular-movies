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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.julia.popularmovies.details.FetchFavoritesTask;
import com.example.julia.popularmovies.models.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieGridFragment extends Fragment {

    private final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    private MovieGridAdapter mMovieGridAdapter;
    private ArrayList<Movie> mMovies;
    private String mSortBy = Config.POPULARITY_DESC;

    @BindView(R.id.gridview_movies)
    RecyclerView mGridRecyclerView;

    // newInstance constructor for creating fragment with arguments
    public static MovieGridFragment newInstance(String str) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle args = new Bundle();
        args.putString(Config.SORT_SETTING_KEY, str);
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mSortBy = getArguments().getString(Config.SORT_SETTING_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies(mSortBy);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mMovieGridAdapter = new MovieGridAdapter(getContext(), new ArrayList<Movie>());
        mGridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getGridColsNumber()));
        mGridRecyclerView.setAdapter(mMovieGridAdapter);
        return view;
    }

    private int getGridColsNumber() {
        int gridColsNumber = getResources().getInteger(R.integer.grid_number_cols);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridColsNumber = getResources().getInteger(R.integer.grid_number_cols_portrait);
        }
        return gridColsNumber;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateMovies(mSortBy);
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies(String sortBy) {
        if (mSortBy.equals(Config.FAVORITES)) {
            new FetchFavoritesTask(getActivity(), mMovieGridAdapter, mMovies).execute();
        } else {
            new FetchMoviesTask(mMovieGridAdapter).execute(sortBy);
        }
    }

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