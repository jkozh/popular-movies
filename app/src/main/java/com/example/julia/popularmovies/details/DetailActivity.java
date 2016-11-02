/*
 *
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
 *
 */

package com.example.julia.popularmovies.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.julia.popularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity /*implements DetailFragment.Listener*/ {

    private ImageView mBackdropView;
    private FloatingActionButton playFAB;
    private AppBarLayout appBar;
    private static final String ICON_PLAY = "ICON_PLAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_MOVIE,
                    getIntent().getParcelableExtra(DetailFragment.DETAIL_MOVIE));

            DetailFragment fragment = new DetailFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
        mBackdropView = (ImageView) findViewById(R.id.detail_backdrop);
        playFAB = (FloatingActionButton) findViewById(R.id.play_FAB);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public void onBackdropFetched(String str) {
        if (str != null) {
            Picasso.with(getApplicationContext())
                    .load(str)
                    .into(mBackdropView);
        } else {
            appBar.setExpanded(false);
        }
    }

    @Override
    public void onTrailersFetched(final Uri trailer) {
        playFAB.setVisibility(View.VISIBLE);
        playFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(trailer);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (playFAB != null) {
            outState.putBoolean(ICON_PLAY, playFAB.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(ICON_PLAY)) {
                playFAB.setVisibility(View.VISIBLE);
            }
        }
    }
}