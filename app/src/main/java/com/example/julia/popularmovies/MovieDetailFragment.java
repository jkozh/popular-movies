package com.example.julia.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class MovieDetailFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] movieStr;
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            movieStr = intent.getStringArrayExtra(Intent.EXTRA_TEXT);

            ((TextView) rootView.findViewById(R.id.detail_title))
                    .setText(movieStr[0]);
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(movieStr[1]);

            new DownloadImageTask((ImageView) rootView.findViewById(R.id.detail_poster))
                    .execute(movieStr[2]);

            ((TextView) rootView.findViewById(R.id.detail_vote_average))
                    .setText(movieStr[3]);
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(movieStr[4]);
        }

        return rootView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
