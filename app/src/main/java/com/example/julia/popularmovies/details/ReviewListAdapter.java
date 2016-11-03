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

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.julia.popularmovies.R;
import com.example.julia.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private final ArrayList<Review> mReviews;

    ReviewListAdapter(ArrayList<Review> reviews) {
        mReviews = reviews;
    }

    public void add(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    ArrayList<Review> getReviews() {
        return mReviews;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        holder.mReview = review;
        holder.mContentView.setText(review.getContent());
        holder.mAuthorView.setText(review.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        final View mView;
        TextView mContentView;
        TextView mAuthorView;
        Review mReview;

        ViewHolder(View view) {
            super(view);
            cardview = (CardView) view.findViewById(R.id.cardview_detail_reviews);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.review_content);
            mAuthorView = (TextView) view.findViewById(R.id.review_author);
        }
    }
}
