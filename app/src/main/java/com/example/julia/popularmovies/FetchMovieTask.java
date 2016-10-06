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
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.julia.popularmovies.data.MoviesContract;
import com.example.julia.popularmovies.data.MoviesContract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

class FetchMovieTask extends AsyncTask<Void, Void, Movie[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context mContext;
    private MovieAdapter mMovieAdapter;

    FetchMovieTask(Context context, MovieAdapter movieAdapter) {
        mContext = context;
        mMovieAdapter = movieAdapter;
    }


    private String sortBy() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        String sortType = prefs.getString(
                mContext.getString(R.string.pref_sort_key),
                mContext.getString(R.string.pref_sort_popular));
        String sort = "popular";
        if (sortType.equals(mContext.getString(R.string.pref_sort_rating))) {
            sort = "top_rated";
        } else if (!sortType.equals(mContext.getString(R.string.pref_sort_popular))) {
            Log.d(LOG_TAG, "Unit type not found: " + sortType);
        }
        return sort;
    }

    @Override
    protected Movie[] doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        try {
            String sort_by = sortBy();

            Uri builtUri = Uri.parse(Config.MOVIE_BASE_URL).buildUpon()
                    .appendPath(sort_by)
                    .appendQueryParameter(Config.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("MovieFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MovieFragment", "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the movies.
        return null;
    }

    private Movie[] getMovieDataFromJson(String movieJsonStr) throws JSONException {
        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(Config.TMD_LIST);
            int moviesLength = movieArray.length();
            ContentValues movieValuesArr[] = new ContentValues[moviesLength];

            for (int i = 0; i < moviesLength; i++){
                JSONObject movie = movieArray.getJSONObject(i);
                movieValuesArr[i] = new ContentValues();
                movieValuesArr[i].put(
                        MovieEntry.COLUMN_TITLE,
                        movie.getString(Config.TMD_TITLE));
                movieValuesArr[i].put(
                        MovieEntry.COLUMN_POSTER,
                        movie.getString(Config.TMD_POSTER));
                movieValuesArr[i].put(
                        MovieEntry.COLUMN_RATING,
                        movie.getString(Config.TMD_RATING));
                movieValuesArr[i].put(
                        MovieEntry.COLUMN_DATE,
                        movie.getString(Config.TMD_DATE));
                movieValuesArr[i].put(
                        MovieEntry.COLUMN_PLOT,
                        movie.getString(Config.TMD_PLOT));
            }
            // bulkInsert our ContentValues array
            mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, movieValuesArr);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
/*
    @Override
    protected void onPostExecute(Movie[] result) {
        if (result != null && mMovieAdapter != null) {
            mMovieAdapter.clear();
            for (Movie movie: result) {
                mMovieAdapter.add(movie);
            }
        } else {
            Toast.makeText(
                    mContext,
                    "Something went wrong, please check your internet connection and try again!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }*/
}