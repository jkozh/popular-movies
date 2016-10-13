package com.example.julia.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.julia.popularmovies.data.MoviesContract;

import java.util.ArrayList;
import java.util.List;

class FetchFavoritesTask extends AsyncTask<Void, Void, List<Movie>> {

    private Context mContext;
    private MovieGridAdapter mMovieGridAdapter;
    private ArrayList<Movie> mMovies;

    FetchFavoritesTask(Context context, MovieGridAdapter adapter, ArrayList<Movie> movies) {
        mContext = context;
        mMovieGridAdapter = adapter;
        mMovies = movies;
    }

    private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        List<Movie> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                results.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return results;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.MovieEntry.CONTENT_URI,
                null, // TODO: change?
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            if (mMovieGridAdapter != null) {
                mMovieGridAdapter.setData(movies);
            }
            mMovies = new ArrayList<>();
            mMovies.addAll(movies);
        }
    }
}
