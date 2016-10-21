/*
 *
 *  * Copyright 2016.  Julia Kozhukhovskaya
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.example.julia.popularmovies.models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.data.MoviesContract;
import com.example.julia.popularmovies.details.DetailFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Movie implements Parcelable {

    private final String LOG_TAG = Movie.class.getSimpleName();

    private long mId;
    private String mPoster;   // poster's path
    private String mTitle;    // original title
    private String mDate;     // release date
    private String mRating;   // vote average
    private String mPlot;     // overview (synopsis)
    private String mBackdrop; // backdrop_path

    public Movie(JSONObject movie) throws JSONException {
        mId = movie.getLong(Config.TMD_ID);
        mPoster = movie.getString(Config.TMD_POSTER);
        mTitle = movie.getString(Config.TMD_TITLE);
        mDate = movie.getString(Config.TMD_DATE);
        mRating = movie.getString(Config.TMD_RATING);
        mPlot = movie.getString(Config.TMD_PLOT);
        mBackdrop = movie.getString(Config.TMD_BACKDROP);
    }

    public Movie(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_ID));
        mPoster = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_POSTER));
        mTitle = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_TITLE));
        mDate = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_DATE));
        mRating = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_RATING));
        mPlot = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_PLOT));
        mBackdrop = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_BACKDROP));
    }

    public long getId() {
        return mId;
    }

    public String getPoster() {
        if (mPoster != null && !mPoster.isEmpty()) {
            return mPoster;
        }
        return null;
    }

    public String getImagePath(Context context, float dp, String path) {
        return Config.POSTER_URL + getImageSizeParam(context, dp) + path;

    }

    public String getBackdrop() {
        if (mBackdrop != null && !mBackdrop.isEmpty()) {
            return mBackdrop;
        }
        return null;
    }

    //((Activity) context).findViewById(R.id.image_play_icon_backdrop).setVisibility(View.VISIBLE);
    // getImageSizeParam(context, context.getResources().getDisplayMetrics().densityDpi)

    private String getImageSizeParam(Context context, float dp) {
        // 120dp for posters
        // full dp for backdrops
        float widthPx = dp * context.getResources().getDisplayMetrics().density;
        String widthParam;

        if (widthPx <= 92) {
            widthParam = "w92";
        } else if (widthPx <= 154) {
            widthParam = "w154";
        } else if (widthPx <= 185) {
            widthParam = "w185";
        } else if (widthPx <= 342) {
            widthParam = "w342";
        } else if (widthPx <= 500) {
            widthParam = "w500";
        } else {
            widthParam = "w780";
        }
        return widthParam;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getDate(Context context) {
        String date = mDate;
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        if (date != null && !date.isEmpty()) {
            try {
                return DateFormat.getDateInstance().format(inputFormat.parse(date));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "The Release date was not parsed successfully: " + date);
            }
        } else {
            date = context.getString(R.string.release_date_missing);
        }
        return date;
    }

    public String getRating() {
        // rounding rating from #.## to #.#
        double rating = Double.parseDouble(mRating);
        return String.valueOf(Math.round(rating * 10d) / 10d);
    }

    public String getPlot() {
        return mPlot;
    }



    private Movie(Parcel in) {
        mId = in.readLong();
        mPoster = in.readString();
        mTitle = in.readString();
        mDate = in.readString();
        mRating = in.readString();
        mPlot = in.readString();
        mBackdrop = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mPoster);
        parcel.writeString(mTitle);
        parcel.writeString(mDate);
        parcel.writeString(mRating);
        parcel.writeString(mPlot);
        parcel.writeString(mBackdrop);
    }
}