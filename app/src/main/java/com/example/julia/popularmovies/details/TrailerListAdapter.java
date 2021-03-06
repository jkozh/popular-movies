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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.julia.popularmovies.Config;
import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder>  {

    private ArrayList<Trailer> mTrailers;
    private Context mContext;

    TrailerListAdapter(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }

    public void add(Trailer trailer) {
        mTrailers.add(trailer);
        notifyDataSetChanged();
    }

    public void add(ArrayList<Trailer> trailers) {
        mTrailers.clear();
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }

    ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_movie_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        Picasso.with(mContext)
                .load(Config.TMD_TRAILER_YOUTUBE_URL + trailer.getKey() + Config.YOUTUBE_IMAGE_PATH)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getTrailerUrl()));
                /**
                 * If there are no apps on the device that can receive the implicit intent,
                 * your app will crash when it calls startActivity(). To first verify that
                 * an app exists to receive the intent, call resolveActivity() on your Intent object.
                 * If the result is non-null, there is at least one app that can handle the intent
                 * and it's safe to call startActivity(). If the result is null,
                 * you should not use the intent and, if possible,
                 * you should disable the feature that invokes the intent.
                 */
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
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
        @BindView(R.id.trailer_image)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}