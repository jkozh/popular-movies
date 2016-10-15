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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

class TrailerListAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private List<Trailer> mTrailers;

    public TrailerListAdapter(Context context, List<Trailer> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTrailers = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Trailer object) {
        mTrailers.add(object);
        notifyDataSetChanged();
    }

    public void clear() {
        mTrailers.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return Long.parseLong(mTrailers.get(position).getId());
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.item_movie_trailer, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Trailer trailer = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        Picasso.with(getContext())
                .load( "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg")
                .into(viewHolder.imageView);
        viewHolder.nameView.setText(trailer.getName());
        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }
}