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

import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable {

    private String movie_id;
    String posterUrl;
    String title;
    String releaseDate;
    String rating;
    String synopsis;

    Movie(String id, String url, String t, String date, String rat, String synops) {
        movie_id = id;
        posterUrl = url;
        title = t;
        releaseDate = date;
        rating = rat;
        synopsis = synops;
    }

    private Movie(Parcel in) {
        movie_id = in.readString();
        posterUrl = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
        synopsis = in.readString();
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
        parcel.writeString(movie_id);
        parcel.writeString(posterUrl);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(rating);
        parcel.writeString(synopsis);
    }
}