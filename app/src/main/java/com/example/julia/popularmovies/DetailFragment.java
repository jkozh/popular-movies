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

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julia.popularmovies.data.MoviesContract.MovieEntry;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    private Movie mMovie;
    private ShareActionProvider mShareActionProvider;
    private ImageView mPosterView;
    private TextView mTitleView;
    private TextView mPlotView;
    private TextView mDateView;
    private TextView mRatingView;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMovie != null) {
            inflater.inflate(R.menu.detail, menu);
            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            MenuItem action_share = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            action_favorite.setIcon(Utility.isFavorited(
                    getActivity(), mMovie.getId()) ?
                    R.drawable.ic_favorite_white_48dp : R.drawable.ic_favorite_border_white_48dp);

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), mMovie.getId());
                }

                @Override
                protected void onPostExecute(Boolean isFavorited) {
                    action_favorite.setIcon(
                            isFavorited ?
                                    R.drawable.ic_favorite_white_48dp :
                                    R.drawable.ic_favorite_border_white_48dp);
                }
            }.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite: {
                Toast.makeText(getActivity(), "Pressed", Toast.LENGTH_SHORT).show();

                if (mMovie != null) {
                    Toast.makeText(getActivity(), "Pressed favorite", Toast.LENGTH_SHORT).show();

                    // check if movie is in favorites or not
                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), mMovie.getId());
                        }

                        @Override
                        protected void onPostExecute(Boolean isFavorited) {
                            // if it is in favorites
                            if (isFavorited) {
                                // delete from favorites
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                MovieEntry.CONTENT_URI,
                                                MovieEntry.COLUMN_ID + " = ?",
                                                new String[]{Long.toString(mMovie.getId())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.ic_favorite_border_white_48dp);
                                        Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();
                            }
                            // if it is not in favorites
                            else {
                                // add to favorites
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();

                                        values.put(MovieEntry.COLUMN_ID, mMovie.getId());
                                        values.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                                        values.put(MovieEntry.COLUMN_DATE, mMovie.getDate(getContext()));
                                        values.put(MovieEntry.COLUMN_PLOT, mMovie.getPlot());
                                        values.put(MovieEntry.COLUMN_POSTER, mMovie.getPoster(getContext()));
                                        values.put(MovieEntry.COLUMN_RATING, mMovie.getRating());

                                        return getActivity().getContentResolver().insert(
                                                MovieEntry.CONTENT_URI, values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.ic_favorite_white_48dp);
                                        Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Config.DETAIL_MOVIE);
            if (mMovie == null) {
                Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "not null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(LOG_TAG, "Null Arguments");
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mPosterView = (ImageView) rootView.findViewById(R.id.detail_poster);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mPlotView = (TextView) rootView.findViewById(R.id.detail_plot);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date);
        mRatingView = (TextView) rootView.findViewById(R.id.detail_rating);


        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            // Set Movie Release date
            ((TextView) rootView.findViewById(R.id.detail_date))
                    .setText(movie.getDate(getContext()));

            // Set Movie Poster
            Picasso.with(getContext())
                    .load(movie.getPoster(getContext()))
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