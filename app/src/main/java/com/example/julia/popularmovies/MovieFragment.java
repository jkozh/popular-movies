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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import static com.example.julia.popularmovies.data.MoviesDbHelper.LOG_TAG;


public class MovieFragment extends Fragment {

    private MovieGridAdapter mMovieGridAdapter;
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
            updateMovie();
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
        GridView mGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());
        mGridView.setAdapter(mMovieGridAdapter);

    // Think about it
/*        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieGridAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });
*/
        updateMovie();
        return rootView;
    }

    // TODO: think about this func
    private String sortBy() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String sortType = prefs.getString(
                getContext().getString(R.string.pref_sort_key),
                getContext().getString(R.string.pref_sort_popular));
        String sort = Config.POPULAR;
        if (sortType.equals(getContext().getString(R.string.pref_sort_rating))) {
            sort = Config.TOP_RATED;
        } else if (!sortType.equals(getContext().getString(R.string.pref_sort_popular))) {
            Log.d(LOG_TAG, "Unit type not found: " + sortType);
        }
        return sort;
    }

    private void updateMovie() {
        // TODO: get sort_by parameter here
        new FetchMovieTask(getContext(), mMovieGridAdapter).execute("popularity.desc");
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }
}