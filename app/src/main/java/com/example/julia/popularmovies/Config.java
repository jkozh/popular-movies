package com.example.julia.popularmovies;

class Config {

    // Paths used in the JSON String
    static final String TMD_LIST = "results";
    static final String TMD_POSTER = "poster_path";
    static final String TMD_TITLE = "title";
    static final String TMD_DATE = "release_date";
    static final String TMD_VOTE = "vote_average";
    static final String TMD_PLOT = "overview";

    // Constructing an URL for a movie's poster image
    static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    // JSON URL
    static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    static final String API_KEY_PARAM = "api_key";
}