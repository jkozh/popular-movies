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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.julia.popularmovies.details.DetailActivity;
import com.example.julia.popularmovies.details.DetailFragment;
import com.example.julia.popularmovies.models.Movie;

public class MovieGridActivity extends AppCompatActivity implements MovieGridAdapter.Listener,
        TabLayout.OnTabSelectedListener, DetailFragment.Listener {

    private boolean mTwoPane;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    final private String[] FRAGMENT_NAME = { "Popular", "Top Rated", "Favorite" };
    final private String TOOLBAR_TITLE = "TOOLBAR_TITLE";

    public MovieGridActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        mTwoPane = (findViewById(R.id.detail_fragment_framelayout) != null);
        // Initialize Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(savedInstanceState);
        // Initialize ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // Setup ViewPager Adapter
        setupViewPager(mViewPager);
        // TabLayout initialization
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        // Setup Listeners to Tabs
        mTabLayout.addOnTabSelectedListener(this);

        if (mTwoPane) {
            FrameLayout layout = (FrameLayout) findViewById(R.id.detail_fragment_framelayout);
            layout.setBackgroundResource(R.drawable.detailview_placeholder);
        }
    }

    private void setupToolbar(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            if (savedInstanceState != null) {
                getSupportActionBar().setTitle(getString(R.string.format_toolbar_title,
                        savedInstanceState.getCharSequence(TOOLBAR_TITLE)));
            } else {
                getSupportActionBar().setTitle(
                        getString(R.string.format_toolbar_title, FRAGMENT_NAME[0]));
            }
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_MOVIE, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_framelayout, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putCharSequence(TOOLBAR_TITLE, FRAGMENT_NAME[mTabLayout.getSelectedTabPosition()]);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mToolbar.setTitle(getString(R.string.format_toolbar_title,
                    savedInstanceState.getCharSequence(TOOLBAR_TITLE)));
        }
    }

    // This method will call Adapter for ViewPager
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MovieGridFragment.newInstance(Config.POPULARITY_DESC), FRAGMENT_NAME[0]);
        adapter.addFragment(MovieGridFragment.newInstance(Config.RATING_DESC), FRAGMENT_NAME[1]);
        adapter.addFragment(MovieGridFragment.newInstance(Config.FAVORITES), FRAGMENT_NAME[2]);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mToolbar.setTitle(getString(
                R.string.format_toolbar_title, FRAGMENT_NAME[tab.getPosition()]));
        // When Tab is clicked this line set the viewpager to corresponding fragment
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    public void backdropUrl(String url) {
    }

    @Override
    public void trailerUri(Uri uri) {
    }
}