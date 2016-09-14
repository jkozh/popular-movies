package com.example.julia.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    String posterUrl;
    String title;
    String releaseDate;
    String voteAverage;
    String overview;

    protected Movie(Parcel in) {
        posterUrl = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        overview = in.readString();

    }

    public Movie(String url, String  t, String date, String vote, String plot) {
        posterUrl = url;
        title = t;
        releaseDate = date;
        voteAverage = vote;
        overview = plot;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(overview);
    }
}