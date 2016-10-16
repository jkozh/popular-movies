/*
 * Copyright 2016.  Julia Kozhukhovskaya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.julia.popularmovies.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder>  {

    private List<Trailer> mTrailers;

    TrailerListAdapter(List<Trailer> trailers) {
        mTrailers = trailers;
    }

    public void add(Trailer trailer) {
        mTrailers.add(trailer);
        notifyDataSetChanged();
    }

    void clear() {
        mTrailers.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        Picasso.with(holder.imageView.getContext())
                .load(Config.TMD_TRAILER_YOUTUBE_URL + trailer.getKey() + Config.YOUTUBE_IMAGE_PATH)
                .into(holder.imageView);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
        }
    }
}