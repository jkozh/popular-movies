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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.julia.popularmovies.details.DetailActivity;
import com.example.julia.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends BaseAdapter {

    private final static String LOG_TAG = MovieGridAdapter.class.getSimpleName();
    private Context mContext;
    private List<Movie> mMovies;
    private final LayoutInflater mInflater;

    MovieGridAdapter(Activity context, List<Movie> movies){
        mContext = context;
        mMovies = movies;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.grid_item_movie, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Movie movie = getItem(position);
        viewHolder = (ViewHolder) view.getTag();
        Picasso.with(getContext()).load(movie.getPoster(getContext())).into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    private void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void add(Movie movie) {
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void setData(List<Movie> movies) {
        clear();
        for (Movie movie: movies) {
            add(movie);
        }
    }

    public static class ViewHolder {
        public final ImageView image;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.grid_item_movie_image);
        }
    }


}