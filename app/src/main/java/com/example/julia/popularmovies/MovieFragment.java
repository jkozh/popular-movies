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
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MovieFragment extends Fragment {

    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> movieList;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movieList = new ArrayList<>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMovieTask movieTask = new FetchMovieTask(getContext(), mMovieAdapter);
            movieTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Saves state of activity as parcelable.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final int NUMBER_COLUMNS = 2;

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.gridview_movie);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(getActivity(), movieList);
        recyclerView.setAdapter(mMovieAdapter);
        updateMovie();
        return rootView;
    }

    private void updateMovie() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity(), mMovieAdapter);
        movieTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }
}