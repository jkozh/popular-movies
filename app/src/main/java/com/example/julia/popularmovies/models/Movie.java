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
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.MovieGridActivity;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.data.MoviesContract;
import com.example.julia.popularmovies.details.FetchMovieInfoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
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
    private String mGenres;   // genres ids
    private String mRuntime;

    public Movie() {
    }

    public Movie(JSONObject movie) throws JSONException {
        mId = movie.getLong(Config.TMD_ID);
        mPoster = movie.getString(Config.TMD_POSTER);
        mTitle = movie.getString(Config.TMD_TITLE);
        mDate = movie.getString(Config.TMD_DATE);
        mRating = movie.getString(Config.TMD_RATING);
        mPlot = movie.getString(Config.TMD_PLOT);
        mBackdrop = movie.getString(Config.TMD_BACKDROP);
        mGenres = movie.getString(Config.TMD_GENRES);
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
        mGenres = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_GENRES));
        mRuntime = cursor.getString(cursor.getColumnIndexOrThrow(
                MoviesContract.MovieEntry.COLUMN_RUNTIME));
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

    public String getImagePath(Context context, String path) {
        return Config.POSTER_URL + getImageSizeParam(context) + path;
    }

    public String getBackdrop() {
        if (mBackdrop != null && !mBackdrop.isEmpty()) {
            return mBackdrop;
        }
        return null;
    }

    private String getImageSizeParam(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) return "w780";
        if (density >= 3.0) return "w500";
        if (density >= 2.0) return "w342";
        if (density >= 1.5) return "w185";
        return "w154";
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
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(Math.round(rating * 10d) / 10d);
    }

    public String getPlot() {
        return mPlot;
    }

    public String getGenres() {
        return mGenres;
    }

    public String getReadableGenres() {
        if (getGenres().length() > 2) {
            String[] genreIds = getGenres().substring(1, getGenres().length() - 1).split(",");
            String genres = "";
            for (String value : genreIds) {
                switch (Integer.parseInt(value)) {
                    case 28:
                        genres += "Action";
                        break;
                    case 12:
                        genres += "Adventure";
                        break;
                    case 16:
                        genres += "Animation";
                        break;
                    case 35:
                        genres += "Comedy";
                        break;
                    case 80:
                        genres += "Crime";
                        break;
                    case 99:
                        genres += "Documentary";
                        break;
                    case 18:
                        genres += "Drama";
                        break;
                    case 10751:
                        genres += "Family";
                        break;
                    case 14:
                        genres += "Fantasy";
                        break;
                    case 36:
                        genres += "History";
                        break;
                    case 27:
                        genres += "Horror";
                        break;
                    case 10402:
                        genres += "Music";
                        break;
                    case 9648:
                        genres += "Mystery";
                        break;
                    case 10749:
                        genres += "Romance";
                        break;
                    case 878:
                        genres += "Science Fiction";
                        break;
                    case 10770:
                        genres += "TV Movie";
                        break;
                    case 53:
                        genres += "Thriller";
                        break;
                    case 10752:
                        genres += "War";
                        break;
                    case 37:
                        genres += "Western";
                        break;
                    default:
                        genres += "Unknown";
                        break;
                }
                genres += " | ";
            }
            return genres.substring(0, genres.length() - 3);
        }
        return null;
    }

    public String getRuntime() {
        if (mRuntime != null && !mRuntime.isEmpty()) {
            return mRuntime;
        }
        return null;
    }

    public String getReadableRuntime() {
        if (getRuntime() != null && !getRuntime().equals("0")) {
            int t = Integer.parseInt(getRuntime());
            int hours = t / 60;
            int minutes = t % 60;
            if (hours == 0) {
                return minutes + "m";
            } else if(minutes == 0) {
                return hours + "h";
            }
            return hours + "h " + minutes + "m";
        }
        return "unavailable";
    }

    public void setRuntime(String runtime) {
        mRuntime = runtime;
    }

    private Movie(Parcel in) {
        mId = in.readLong();
        mPoster = in.readString();
        mTitle = in.readString();
        mDate = in.readString();
        mRating = in.readString();
        mPlot = in.readString();
        mBackdrop = in.readString();
        mGenres = in.readString();
        mRuntime = in.readString();
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
        parcel.writeString(mGenres);
        parcel.writeString(mRuntime);
    }
}