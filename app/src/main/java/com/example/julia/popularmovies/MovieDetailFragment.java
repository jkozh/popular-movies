package com.example.julia.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class MovieDetailFragment extends Fragment {

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(movie.title);
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(movie.releaseDate);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            final ImageView  mImageView = (ImageView ) rootView.findViewById(R.id.detail_poster);
            ImageRequest request = new ImageRequest(movie.posterUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            mImageView.setImageResource(R.drawable.image_load_error);
                        }
                    });
            queue.add(request);

            ((TextView) rootView.findViewById(R.id.detail_vote_average))
                    .setText(movie.voteAverage);
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(movie.overview);
        }
        return rootView;
    }
}