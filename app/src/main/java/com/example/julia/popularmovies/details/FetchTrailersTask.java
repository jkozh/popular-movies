/*
 * Copyright 2016.  Julia Kozhukhovskaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.julia.popularmovies.details;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.julia.popularmovies.BuildConfig;
import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.models.Review;
import com.example.julia.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private final Listener mListener;

    // Interface definition for a callback to be invoked when trailers are loaded
    interface Listener {
        void onTrailersFetchFinished(ArrayList<Trailer> trailers);
    }

    FetchTrailersTask(Listener listener) {
        mListener = listener;
    }

    private ArrayList<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
        JSONObject trailerJson = new JSONObject(jsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray(Config.TMD_LIST);
        ArrayList<Trailer> results = new ArrayList<>();
        for(int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailer = trailerArray.getJSONObject(i);
            // Show Trailers that on Youtube only
            if (trailer.getString(Config.TMD_TRAILER_SITE).contentEquals(Config.TMD_TRAILER_YOUTUBE)) {
                Trailer trailerModel = new Trailer(trailer);
                results.add(trailerModel);
            }
        }
        return results;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        if (params.length == 0) {
            Log.e(LOG_TAG, "Length of params equals zero");
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        try {
            Uri builtUri = Uri.parse(Config.MOVIE_BASE_URL).buildUpon()
                    .appendPath(Config.MOVIE_PARAM)
                    .appendPath(params[0])
                    .appendPath(Config.VIDEOS_PARAM)
                    .appendQueryParameter(Config.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) { return null; }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) { return null; }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getTrailersDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        if (trailers != null) {
            mListener.onTrailersFetchFinished(trailers);
        } else {
            mListener.onTrailersFetchFinished(new ArrayList<Trailer>());
        }
    }
}
