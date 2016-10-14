/** Copyright 2016.  Julia Kozhukhovskaya
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

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julia.popularmovies.data.MoviesContract;

import java.util.ArrayList;
import java.util.List;

class FetchFavoritesTask extends AsyncTask<Void, Void, List<Movie>> {

    private final String LOG_TAG = FetchFavoritesTask.class.getSimpleName();

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
                Log.e(LOG_TAG, Long.toString(movie.getId()));
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
                null,
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