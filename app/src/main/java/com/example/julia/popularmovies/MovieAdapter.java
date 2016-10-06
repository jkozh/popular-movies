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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julia.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final TextView title;
        public final ImageView poster;
        public final TextView plot;
        public final TextView rating;
        public final TextView date;


        public ViewHolder(View view){
            title = (TextView) view.findViewById((R.id.detail_title));
            poster = (ImageView) view.findViewById(R.id.detail_poster);
            plot = (TextView) view.findViewById(R.id.detail_plot);
            rating = (TextView) view.findViewById(R.id.detail_rating);
            date = (TextView) view.findViewById(R.id.detail_date);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.grid_item_movie;

        Log.d(LOG_TAG, "In new View");

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");

        int titleIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE);
        viewHolder.title.setText(cursor.getString(titleIndex));

        int imageIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER);
        Picasso.with(context)
                .load(Config.MOVIE_POSTER_BASE_URL + cursor.getInt(imageIndex))
                .into(viewHolder.poster);

        Log.i(LOG_TAG, "Image reference extracted: " + cursor.getInt(imageIndex));

        int plotIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_PLOT);
        viewHolder.plot.setText(cursor.getString(plotIndex));

        int ratingIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING);
        viewHolder.rating.setText(cursor.getString(ratingIndex));

        int dateIndex = cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_DATE);
        viewHolder.date.setText(cursor.getString(dateIndex));

    }
}