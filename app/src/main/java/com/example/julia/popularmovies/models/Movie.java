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

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.data.MoviesContract;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable {

    private long mId;
    private String mPoster; // poster's path
    private String mTitle;  // original title
    private String mDate;   // release date
    private String mRating; // vote average
    private String mPlot;   // overview/synopsis

    public Movie(JSONObject movie) throws JSONException {
        mId = movie.getLong(Config.TMD_ID);
        mPoster = movie.getString(Config.TMD_POSTER);
        mTitle = movie.getString(Config.TMD_TITLE);
        mDate = movie.getString(Config.TMD_DATE);
        mRating = movie.getString(Config.TMD_RATING);
        mPlot = movie.getString(Config.TMD_PLOT);
    }

    public Movie(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_ID));
        mPoster = cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_POSTER));
        mTitle = cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_TITLE));
        mDate = cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_DATE));
        mRating = cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_RATING));
        mPlot = cursor.getString(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_PLOT));
    }

    public long getId() {
        return mId;
    }

    public String getPoster(Context context) {
        if (mPoster != null && !mPoster.isEmpty()) {
            return context.getResources().getString(R.string.poster_url) + mPoster;
        }
        return mPoster;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
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
    }
}