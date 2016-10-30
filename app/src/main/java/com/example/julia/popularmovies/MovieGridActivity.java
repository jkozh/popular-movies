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
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.julia.popularmovies.details.DetailActivity;
import com.example.julia.popularmovies.details.DetailFragment;
import com.example.julia.popularmovies.models.Movie;

public class MovieGridActivity extends AppCompatActivity implements MovieGridAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    private boolean mTwoPane;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    final private String[] FRAGMENT_NAME = { "Popular", "Top Rated", "Favorite" };
    final private String TOOLBAR_TITLE = "TOOLBAR_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Setting up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            if (savedInstanceState != null) {
                getSupportActionBar().setTitle(getString(R.string.format_toolbar_title,
                        savedInstanceState.getCharSequence(TOOLBAR_TITLE)));
            } else {
                getSupportActionBar().setTitle(getString(R.string.format_toolbar_title, FRAGMENT_NAME[0]));

            }
        }
        //Initialize ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //Setup ViewPager Adapter
        setupViewPager(mViewPager);
        //TabLayout initialization
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mTabLayout.setupWithViewPager(mViewPager);
        //setup Listeners to Tabs
        mTabLayout.addOnTabSelectedListener(this);

        if (findViewById(R.id.detail_fragment) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment,  new DetailFragment())
                        .commit();
            }
        }else {
            mTwoPane = false;
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
        mToolbar.setTitle(getString(R.string.format_toolbar_title, FRAGMENT_NAME[tab.getPosition()]));
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
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_MOVIE, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}