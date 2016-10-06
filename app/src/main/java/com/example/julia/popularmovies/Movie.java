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

public class Movie {

    String _id;
    String poster;
    String title;
    String date;
    String rating;
    String plot;

    public Movie(String _id, String poster, String title, String date, String rating, String plot) {
        this._id = _id;
        this.poster = poster;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.plot = plot;
    }
}