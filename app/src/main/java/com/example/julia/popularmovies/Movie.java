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