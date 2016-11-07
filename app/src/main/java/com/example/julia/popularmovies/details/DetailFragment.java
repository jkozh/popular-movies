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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.julia.popularmovies.MovieGridFragment;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.data.MoviesContract.MovieEntry;
import com.example.julia.popularmovies.models.Movie;
import com.example.julia.popularmovies.models.Review;
import com.example.julia.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements FetchTrailersTask.Listener,
        FetchReviewsTask.Listener, FetchMovieInfoTask.Listener {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    private static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";
    private static final String TRAILERS_VIEW = "TRAILERS_VIEW";
    private static final String REVIEWS_VIEW = "REVIEWS_VIEW";
    private static final String EXTRA_RUNTIME = "EXTRA_RUNTIME";

    private Movie mMovie;
    private TrailerListAdapter mTrailerListAdapter;
    private ReviewListAdapter mReviewListAdapter;
    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.detail_poster)
    ImageView mPosterView;
    @BindView(R.id.detail_title)
    TextView mTitleView;
    @BindView(R.id.detail_plot)
    TextView mPlotView;
    @BindView(R.id.detail_date)
    TextView mDateView;
    @BindView(R.id.detail_rating)
    TextView mRatingView;
    @BindView(R.id.detail_genres)
    TextView mGenresView;
    @BindView(R.id.trailers_view)
    LinearLayout mTrailersView;
    @BindView(R.id.detail_runtime)
    TextView mRuntimeView;
    @BindView(R.id.detail_reviews_title)
    TextView mReviewsTitleView;
    @BindView(R.id.detail_backdrop)
    ImageView mBackdropView;
    @BindView(R.id.detail_trailers)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.detail_reviews)
    RecyclerView mReviewsRecyclerView;
    @BindDrawable(R.drawable.ic_theaters_black_48dp)
    Drawable mPlaceHolderPoster;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;


    private Listener mListener;

    public interface Listener {
        boolean isTwoPane();
        void backdropUrl(String url);
        void trailerUri(final Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new ClassCastException("activity must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(DETAIL_MOVIE);
        } else {
            Log.e(LOG_TAG,"args are null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTrailerListAdapter != null) {
            ArrayList<Trailer> trailers = mTrailerListAdapter.getTrailers();
            if (trailers != null && !trailers.isEmpty()) {
                outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
            }
        } else {
            Log.e(LOG_TAG, "mTrailerListAdapter is null");
        }
        if (mTrailersView != null) {
            outState.putBoolean(TRAILERS_VIEW, mTrailersView.getVisibility() == View.VISIBLE);
        }
        if (mReviewListAdapter != null) {
            ArrayList<Review> reviews = mReviewListAdapter.getReviews();
            if (reviews != null && !reviews.isEmpty()) {
                outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
            }
        } else {
            Log.e(LOG_TAG, "mReviewListAdapter is null");
        }
        if (mReviewsRecyclerView != null) {
            outState.putBoolean(REVIEWS_VIEW, mReviewsRecyclerView.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(TRAILERS_VIEW)) {
                mTrailersView.setVisibility(View.VISIBLE);
            }
            if (savedInstanceState.getBoolean(REVIEWS_VIEW)) {
                mReviewsRecyclerView.setVisibility(View.VISIBLE);
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
                        protected void onPostExecute(final Boolean isFavored) {
                            // if it is in favorites
                            if (isFavored) {
                                // delete from favorites
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        getActivity().getContentResolver().delete(
                                                MovieEntry.CONTENT_URI,
                                                MovieEntry.COLUMN_ID + " = ?",
                                                new String[]{Long.toString(mMovie.getId())}
                                        );
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void v) {
                                        item.setIcon(R.drawable.ic_favorite_border_white_48dp);
                                        Toast.makeText(getActivity(), "Removed from favorites",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();
                            }
                            // if it's not in favorites - add to favorites
                            else {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
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

                                        getActivity().getContentResolver().insert(
                                                MovieEntry.CONTENT_URI, values);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void v) {
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

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        // horizontal list layout for trailers
        mTrailerListAdapter = new TrailerListAdapter(new ArrayList<Trailer>());
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(horizontalLayoutManager);
        mTrailersRecyclerView.setAdapter(mTrailerListAdapter);

        // vertical list layout for reviews
        mReviewListAdapter = new ReviewListAdapter(new ArrayList<Review>());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
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

            new FetchMovieInfoTask(this).execute(Long.toString(mMovie.getId()));

            if (mListener.isTwoPane()) {
                // Set movie backdrop
                String backdropUrl = mMovie.getBackdrop();
                if (!backdropUrl.equals("null")) {
                    Picasso.with(getContext())
                            .load(mMovie.getImagePath(getContext(), backdropUrl))
                            .into(mBackdropView);
                }
            } else {
                mListener.backdropUrl(mMovie.getImagePath(getContext(), mMovie.getBackdrop()));
            }

            // Set movie poster
            String posterUrl = mMovie.getPoster();
            if (!posterUrl.equals("null")) {
                Picasso.with(getContext())
                        .load(mMovie.getImagePath(getContext(), posterUrl))
                        .into(mPosterView);
            } else {
                mPosterView.setImageDrawable(mPlaceHolderPoster);
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
            // Set movie rating
            setRatingBar();
            mRatingView.setText(getResources().getString(R.string.format_movie_rating, mMovie.getRating()));
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
            mListener.trailerUri(Uri.parse(trailer.getTrailerUrl()));
            View view = getView();
            if (view != null) {
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

    private void setRatingBar() {
        if (mMovie.getRating() != null && !mMovie.getRating().isEmpty()) {
            float rating = Float.valueOf(mMovie.getRating()) / 2;
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