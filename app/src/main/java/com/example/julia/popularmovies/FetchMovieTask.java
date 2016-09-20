package com.example.julia.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchMovieTask {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context context;
    private MovieAdapter movieAdapter;

    public FetchMovieTask(Context context, MovieAdapter movieAdapter) {
        this.context = context;
        this.movieAdapter = movieAdapter;
    }

    private Movie[] getMovieDataFromJson(JSONObject movieJson) throws JSONException {

        JSONArray movieArray = movieJson.getJSONArray(Config.TMD_LIST);
        int moviesLength = movieArray.length();
        Movie[] resultMovies = new Movie[moviesLength];

        for(int i = 0; i < moviesLength; i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            String posterUrl = Config.MOVIE_POSTER_BASE_URL + movie.getString(Config.TMD_POSTER);
            resultMovies[i] = new Movie(
                    posterUrl,
                    movie.getString(Config.TMD_TITLE),
                    movie.getString(Config.TMD_DATE),
                    movie.getString(Config.TMD_VOTE),
                    movie.getString(Config.TMD_PLOT));
        }
        return resultMovies;
    }

    private String sortBy() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        String sortType = prefs.getString(
                context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_popular));
        String sort = "popular";
        if (sortType.equals(context.getString(R.string.pref_sort_rating))) {
            sort = "top_rated";
        } else if (!sortType.equals(context.getString(R.string.pref_sort_popular))) {
            Log.d(LOG_TAG, "Unit type not found: " + sortType);
        }
        return sort;
    }

    protected void getMovies() {
        String sort_by = sortBy();

        Uri builtUri = Uri.parse(Config.MOVIE_BASE_URL).buildUpon()
                .appendPath(sort_by)
                .appendQueryParameter(Config.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        String url = builtUri.toString();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            addMovies(getMovieDataFromJson(response));
                        } catch (JSONException e) {
                            Toast.makeText(
                                    context,
                                    "Unable to fetch data: " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Log.e(LOG_TAG, e.getMessage(), e);
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, error.getMessage(), error);
                    }
                });

        queue.add(jsObjRequest);
    }

    private void addMovies(Movie[] result) {
        if (result != null) {
            movieAdapter.clear();
            for (Movie movie: result) {
                movieAdapter.add(movie);
            }
        } else {
            Toast.makeText(
                    context,
                    "Something went wrong, please check your internet connection and try again!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}