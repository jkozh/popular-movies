package com.example.julia.popularmovies;

public class Config {

    // Paths used in the JSON String
    public static final String TMD_LIST = "results";
    public static final String TMD_POSTER = "poster_path";
    public static final String TMD_TITLE = "title";
    public static final String TMD_DATE = "release_date";
    public static final String TMD_VOTE = "vote_average";
    public static final String TMD_PLOT = "overview";

    // Constructing an URL for a movie's poster image
    public static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    // JSON URL
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY_PARAM = "api_key";
}