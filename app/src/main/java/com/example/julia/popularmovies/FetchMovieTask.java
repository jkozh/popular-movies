package com.example.julia.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMovieTask extends AsyncTask<Void, Void, Movie[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context context;
    private MovieAdapter movieAdapter;

    public FetchMovieTask(Context context, MovieAdapter movieAdapter) {
        this.context = context;
        this.movieAdapter = movieAdapter;
    }

    public Movie[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

        final String TMD_LIST = "results";
        final String TMD_POSTER = "poster_path";
        final String TMD_TITLE = "title";
        final String TMD_DATE = "release_date";
        final String TMD_VOTE = "vote_average";
        final String TMD_PLOT = "overview";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(TMD_LIST);

        Movie[] resultMovies = new Movie[movieArray.length()];

        //  Constructing an URL for a movie's poster image
        final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String[] POSTER_SIZE =
                { "w185", "w92", "w154", "w185", "w342", "w500", "w780", "original" };

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            Uri builtPosterUri = Uri.parse(MOVIE_POSTER_BASE_URL).buildUpon()
                    .appendPath(POSTER_SIZE[3])
                    .appendEncodedPath(movie.getString(TMD_POSTER))
                    .build();

            resultMovies[i] = new Movie(
                    builtPosterUri.toString(),
                    movie.getString(TMD_TITLE),
                    movie.getString(TMD_DATE),
                    movie.getString(TMD_VOTE),
                    movie.getString(TMD_PLOT));
        }
        return resultMovies;
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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            String sortType = prefs.getString(
                    context.getString(R.string.pref_sort_key),
                    context.getString(R.string.pref_sort_popular));

            String sort_by = "popular";

            if (sortType.equals(context.getString(R.string.pref_sort_rating))) {
                sort_by = "top_rated";
            } else if (!sortType.equals(context.getString(R.string.pref_sort_popular))) {
                Log.d(LOG_TAG, "Unit type not found: " + sortType);
            }

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(sort_by)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
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
                buffer.append(line + "\n");
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

    @Override
    protected void onPostExecute(Movie[] result) {
        if (result != null) {
            movieAdapter.clear();
            for (Movie movie: result) {
                movieAdapter.add(movie);
            }
        }
    }
}