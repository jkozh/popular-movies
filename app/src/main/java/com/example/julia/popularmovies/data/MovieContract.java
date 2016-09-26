package com.example.julia.popularmovies.data;

import android.provider.BaseColumns;

public class MovieContract {

    public static String normaliseDate(String date) {
        // TODO: date convert into presentable view
        return "";
    }

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_POSTER = "poster";
        // Rating is stored as a float
        public static final String COLUMN_RATING = "rating";

    }
}
