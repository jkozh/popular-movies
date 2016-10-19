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

public class Config {

    // Paths used in the JSON String
    public static final String TMD_ID = "id";
    public static final String TMD_LIST = "results";
    public static final String TMD_POSTER = "poster_path";
    public static final String TMD_TITLE = "title";
    public static final String TMD_DATE = "release_date";
    public static final String TMD_RATING = "vote_average";
    public static final String TMD_PLOT = "overview";

    // JSON URL
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/";
    static final String DISCOVER_PARAM = "discover";
    public static final String MOVIE_PARAM = "movie";
    static final String SORT_BY_PARAM = "sort_by";
    public static final String API_KEY_PARAM = "api_key";
    public static final String VIDEOS_PARAM = "videos";
    public static final String REVIEWS_PARAM = "reviews";

    // Keys
    static final String SORT_SETTING_KEY = "sort_setting";
    static final String MOVIES_KEY = "movies";

    // Sort options
    static final String POPULARITY_DESC = "popularity.desc";
    static final String RATING_DESC = "vote_average.desc";
    static final String FAVORITE = "favorite";
    static final String SPINNER_KEY = "spinner";
    public static final String DETAIL_MOVIE = "detail_movie";

    // Paths used in JSON for trailers
    public static final String TMD_TRAILER_KEY = "key";
    public static final String TMD_TRAILER_NAME = "name";
    public static final String TMD_TRAILER_SITE = "site";
    public static final String TMD_TRAILER_TYPE = "type";
    public static final String TMD_TRAILER_YOUTUBE = "YouTube";

    public static final String TMD_TRAILER_YOUTUBE_URL = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_IMAGE_PATH = "/0.jpg";
    public static final String TMD_TRAILER_YOUTUBE_WATCH = "http://www.youtube.com/watch?v=";

    public static final String TMD_REVIEW_AUTHOR = "author";
    public static final String TMD_REVIEW_CONTENT = "content";
    public static final String TMD_REVIEW_URL = "url";

}
