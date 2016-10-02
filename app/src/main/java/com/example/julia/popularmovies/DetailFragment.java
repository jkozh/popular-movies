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

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julia.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Cursor mDetailCursor;
    private View mRootView;
    private int mPosition;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private TextView mUriText;
    private Uri mUri;
    private static final int CURSOR_LOADER_ID = 0;

    public static DetailFragment newInstance(int position, Uri uri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        fragment.mUri = uri;
        args.putInt("id", position);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mPoster = (ImageView) rootView.findViewById(R.id.detail_poster);
        mReleaseDate = (TextView) rootView.findViewById(R.id.detail_release_date);
        mRating = (TextView) rootView.findViewById(R.id.detail_vote_average);
        mSynopsis = (TextView) rootView.findViewById(R.id.detail_overview);
        Bundle args = this.getArguments();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, args, DetailFragment.this);


/*
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            // Set Movie Title
            //((TextView) rootView.findViewById(R.id.detail_title))
            //        .setText(movie.title);

            // Set Movie Release date
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(movie.releaseDate);

            // Set Movie Poster
            Picasso.with(getContext())
                    .load(movie.posterUrl)
                    .into((ImageView) rootView.findViewById(R.id.detail_poster));

            // Set Movie Vote Average
            ((TextView) rootView.findViewById(R.id.detail_vote_average))
                    .setText(movie.rating);

            // Set Movie Overview
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(movie.synopsis);
       }
*/
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String [] selectionArgs = null;
        if (args != null){
            selection = MoviesContract.MovieEntry._ID;
            selectionArgs = new String[]{String.valueOf(mPosition)};
        }
        return new CursorLoader(getActivity(),
                mUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDetailCursor = data;
        mDetailCursor.moveToFirst();
        DatabaseUtils.dumpCursor(data);
        //mPoster.setImageResource(mDetailCursor.getString(3));
        mReleaseDate.setText(mDetailCursor.getString(4));
        mRating.setText(mDetailCursor.getString(3));
        mSynopsis.setText(mDetailCursor.getString(2));
        // set Uri to be displayed
        mUriText.setText(mUri.toString());
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mDetailCursor = null;
    }
}