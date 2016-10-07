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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final static String LOG_TAG = MovieListAdapter.class.getSimpleName();
    private Context context;
    private List<Movie> mMovies;

    MovieListAdapter(Activity context, List<Movie> movies){
        this.context = context;
        this.mMovies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Movie movie = mMovies.get(position);
        Picasso.with(context).load(movie.getPoster()).into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public void add(Movie movie) {
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ViewHolder(View view) {
            super(view);
            image = (ImageView)view.findViewById(R.id.grid_item_movie_view);
        }
    }
}