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

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.julia.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private List<Movie> mMovies;

    MovieGridAdapter(Context context, List<Movie> movies){
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mMovies = movies;
    }

    // A callback interface that all activities containing this fragment must implement.
    // This mechanism allows activities to be notified of item selections.
    interface Callback {
        void onItemSelected(Movie movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        int gridColsNumber = parent.getContext().getResources().getInteger(R.integer.grid_number_cols);
        if(parent.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridColsNumber = parent.getContext().getResources().getInteger(R.integer.grid_number_cols_portrait);
        }
        view.getLayoutParams().height = (int) (parent.getWidth() / gridColsNumber * 1.5f);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        String posterUrl = movie.getPoster();
        final Context context = holder.mImageView.getContext();
        if (!posterUrl.equals("null")) {
            Picasso.with(context)
                    .load(movie.getImagePath(context, 120, posterUrl))
                    .into(holder.mImageView);
        } else {
            String uri = context.getString(R.string.icon_theaters_path);
            int imageResource = context.getResources().getIdentifier(uri, null,
                    context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            res.setAlpha(20);
            holder.mImageView.setImageDrawable(res);
        }
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Callback) context).onItemSelected(movie);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.grid_item_movie_image);
        }
    }
}