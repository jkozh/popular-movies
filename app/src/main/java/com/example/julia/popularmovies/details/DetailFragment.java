/*
 *
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
 *
 */

package com.example.julia.popularmovies.details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julia.popularmovies.models.Movie;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.data.MoviesContract.MovieEntry;
import com.example.julia.popularmovies.models.Review;
import com.example.julia.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements FetchTrailersTask.Listener,
        FetchReviewsTask.Listener, FetchMovieInfoTask.Listener {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    private static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";
    private static final String ICON_PLAY_BACKDROP = "ICON_PLAY_BACKDROP";
    private static final String TRAILERS_VIEW = "TRAILERS_VIEW";

    private Movie mMovie;
    private TrailerListAdapter mTrailerListAdapter;
    private ReviewListAdapter mReviewListAdapter;
    private ShareActionProvider mShareActionProvider;
    private RecyclerView mReviewsRecyclerView;
    private ImageView mBackdropView;
    private ImageView mIconPlayBackdrop;
    private LinearLayout mTrailersView;
    private TextView mRuntimeView;
    private TextView mReviewsTitleView;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Trailer> trailers = mTrailerListAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }
        if (mIconPlayBackdrop != null) {
            outState.putBoolean(ICON_PLAY_BACKDROP,
                    mIconPlayBackdrop.getVisibility() == View.VISIBLE);
        }
        if (mTrailersView != null) {
            outState.putBoolean(TRAILERS_VIEW, mTrailersView.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(ICON_PLAY_BACKDROP)) {
                mIconPlayBackdrop.setVisibility(View.VISIBLE);
            }
            if (savedInstanceState.getBoolean(TRAILERS_VIEW)) {
                mTrailersView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMovie != null) {
            inflater.inflate(R.menu.detail, menu);
            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            action_favorite.setIcon(setFavoriteIcon(isFavorited()));

            MenuItem action_share = menu.findItem(R.id.action_share);
            mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    return isFavorited();
                }

                @Override
                protected void onPostExecute(Boolean isFavorited) {
                    action_favorite.setIcon(setFavoriteIcon(isFavorited));
                }
            }.execute();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite: {
                if (mMovie != null) {
                    // check if movie is in favorites or not
                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return isFavorited();
                        }

                        @Override
                        protected void onPostExecute(Boolean isFavored) {
                            // if it is in favorites
                            if (isFavored) {
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
                                        Toast.makeText(getActivity(), "Removed from favorites",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();
                            }
                            // if it's not in favorites - add to favorites
                            else {
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();

                                        values.put(MovieEntry.COLUMN_ID, mMovie.getId());
                                        values.put(MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                                        values.put(MovieEntry.COLUMN_DATE,
                                                mMovie.getDate());
                                        values.put(MovieEntry.COLUMN_PLOT, mMovie.getPlot());
                                        values.put(MovieEntry.COLUMN_POSTER,
                                                mMovie.getPoster());
                                        values.put(MovieEntry.COLUMN_RATING, mMovie.getRating());
                                        values.put(MovieEntry.COLUMN_BACKDROP,
                                                mMovie.getBackdrop());
                                        values.put(MovieEntry.COLUMN_GENRES, mMovie.getGenres());
                                        values.put(MovieEntry.COLUMN_RUNTIME, mMovie.getRuntime());

                                        return getActivity().getContentResolver().insert(
                                                MovieEntry.CONTENT_URI, values);
                                    }
                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.ic_favorite_white_48dp);
                                        Toast.makeText(getActivity(),
                                                "Marked as favorite", Toast.LENGTH_SHORT).show();
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


        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable(DetailFragment.DETAIL_MOVIE);
        } else {
            Log.e(LOG_TAG,"a bundle in onCreateView is null");
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        CoordinatorLayout mDetailLayout = (CoordinatorLayout) rootView.findViewById(R.id.detail_layout);

        if (mMovie != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        mBackdropView = (ImageView) rootView.findViewById(R.id.detail_backdrop);
        ImageView mPosterView = (ImageView) rootView.findViewById(R.id.detail_poster);
        TextView mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        TextView mPlotView = (TextView) rootView.findViewById(R.id.detail_plot);
        TextView mDateView = (TextView) rootView.findViewById(R.id.detail_date);
        TextView mRatingView = (TextView) rootView.findViewById(R.id.detail_rating);
        TextView mGenresView = (TextView) rootView.findViewById((R.id.detail_genres));
        mIconPlayBackdrop = (ImageView) rootView.findViewById(R.id.image_play_icon_backdrop);
        mTrailersView = (LinearLayout) rootView.findViewById(R.id.trailers_view);
        mRuntimeView = (TextView) rootView.findViewById(R.id.detail_runtime);
        mReviewsTitleView = (TextView) rootView.findViewById(R.id.detail_reviews_title);


        // horizontal list layout for trailers
        mTrailerListAdapter = new TrailerListAdapter(new ArrayList<Trailer>());
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mTrailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_trailers);
        mTrailersRecyclerView.setLayoutManager(horizontalLayoutManager);
        mTrailersRecyclerView.setAdapter(mTrailerListAdapter);

        // vertical list layout for reviews
        mReviewListAdapter = new ReviewListAdapter(new ArrayList<Review>());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        mReviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_reviews);
        mReviewsRecyclerView.setLayoutManager(verticalLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewListAdapter);

        if (mMovie != null) {

            // fetch trailers only if there is no trailers fetched yet
            if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
                ArrayList<Trailer> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
                mTrailerListAdapter.add(trailers);
            } else {
                new FetchTrailersTask(this).execute(Long.toString(mMovie.getId()));
            }

            // fetch reviews only if there is no reviews fetched yet
            if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
                List<Review> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
                mReviewListAdapter.add(reviews);
            } else {
                new FetchReviewsTask(this).execute(Long.toString(mMovie.getId()));
            }

            // fetch runtime info for a movie
            new FetchMovieInfoTask(this).execute(Long.toString(mMovie.getId()));

            // Set movie backdrop
            if (!mMovie.getBackdrop().equals("null")) {
                Picasso.with(getContext())
                        .load(mMovie.getImagePath(
                                getContext(),
                                getContext().getResources().getDisplayMetrics().densityDpi,
                                mMovie.getBackdrop()))
                        .into(mBackdropView);
            }
            // Set movie poster
            String posterUrl = mMovie.getPoster();
            if (!posterUrl.equals("null")) {
                Picasso.with(getContext())
                        .load(mMovie.getImagePath(getContext(), 120, posterUrl))
                        .into(mPosterView);
            } else {
                String uri = "@drawable/ic_wallpaper_white_18dp";
                int imageResource = getContext().getResources().getIdentifier(uri, null,
                        getContext().getPackageName());
                Drawable res = getContext().getResources().getDrawable(imageResource);
                mPosterView.setImageDrawable(res);
            }
            // Set movie title
            mTitleView.setText(mMovie.getTitle());
            // Set movie genres
            if (mMovie.getReadableGenres() != null) {
                mGenresView.setText(mMovie.getReadableGenres());
            } else {
                mGenresView.setVisibility(View.GONE);
            }
            // Set movie release date in user-friendly view
            mDateView.setText(mMovie.getDate(getContext()));
            setRatingBar(rootView);
            // Set movie rating
            mRatingView.setText(getResources().getString(R.string.movie_rating, mMovie.getRating()));
            // Set movie overview
            mPlotView.setText(mMovie.getPlot());
        }
        return rootView;
    }

    @Override
    public void onTrailersFetchFinished(ArrayList<Trailer> trailers) {
        mTrailerListAdapter.add(trailers);
        if (mTrailerListAdapter.getItemCount() > 0) {
            Trailer trailer = mTrailerListAdapter.getTrailers().get(0);
            setShareActionProvider(trailer);
            mBackdropView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Trailer trailer = mTrailerListAdapter.getTrailers().get(0);
                    intent.setData(Uri.parse(trailer.getTrailerUrl()));
                    getContext().startActivity(intent);
                }
            });
            View view = getView();
            if (view != null) {
                mIconPlayBackdrop.setVisibility(View.VISIBLE);
                mTrailersView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onReviewsFetchFinished(ArrayList<Review> reviews) {
        mReviewListAdapter.add(reviews);
        if (mReviewListAdapter.getItemCount() > 0) {
            mReviewsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mReviewsTitleView.setText(R.string.no_reviews_found);
        }
    }

    @Override
    public void onMovieInfoFetchFinished(String runtime) {
        if (runtime != null) {
            mMovie.setRuntime(runtime);
            mRuntimeView.setText(mMovie.getReadableRuntime());
        }
    }

    private void setShareActionProvider(Trailer trailer) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovie.getTitle());
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailer.getName() + ": "
                + trailer.getTrailerUrl());
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private int setFavoriteIcon(boolean isFavorited) {
        if (isFavorited) {
            return R.drawable.ic_favorite_white_48dp;
        } else {
            return R.drawable.ic_favorite_border_white_48dp;
        }
    }

    private void setRatingBar(View view) {
        if (mMovie.getRating() != null && !mMovie.getRating().isEmpty()) {
            float rating = Float.valueOf(mMovie.getRating()) / 2;
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            ratingBar.setRating(rating);
        }
    }

    private boolean isFavorited() {
        Cursor cursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                MovieEntry.COLUMN_ID + " = ?",
                new String[] { Long.toString(mMovie.getId()) },
                null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }
}