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
    static final String TMD_LIST = "results";
    public static final String TMD_POSTER = "poster_path";
    public static final String TMD_TITLE = "title";
    public static final String TMD_DATE = "release_date";
    public static final String TMD_RATING = "vote_average";
    public static final String TMD_PLOT = "overview";

    // JSON URL
    static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    static final String SORT_BY_PARAM = "sort_by";
    static final String API_KEY_PARAM = "api_key";

    // Keys
    static final String SORT_SETTING_KEY = "sort_setting";
    static final String MOVIES_KEY = "movies";

    // Sort options
    static final String POPULARITY_DESC = "popularity.desc";
    static final String RATING_DESC = "vote_average.desc";
    static final String FAVORITE = "favorite";
    static final String SPINNER_KEY = "spinner";
    public static final String DETAIL_MOVIE = "detail_movie";
}