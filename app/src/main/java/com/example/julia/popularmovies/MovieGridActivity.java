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
import android.view.Menu;
import android.view.MenuItem;

import com.example.julia.popularmovies.details.DetailActivity;
import com.example.julia.popularmovies.details.DetailFragment;
import com.example.julia.popularmovies.models.Movie;

public class MovieGridActivity extends AppCompatActivity implements MovieGridAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    private boolean mTwoPane;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    final private String[] FRAGMENT_NAME = { "Popular", "Top Rated", "Favorite" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,  new DetailFragment())
                        .commit();
            }
        }else {
            mTwoPane = false;
        }

        // Setting up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.format_toolbar_title, FRAGMENT_NAME[0]));
        setSupportActionBar(mToolbar);

        //Initialize ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //Setup ViewPager Adapter
        setupViewPager(mViewPager);
        //TabLayout initialization
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mTabLayout.setupWithViewPager(mViewPager);
        //setup Listeners to Tabs
        mTabLayout.addOnTabSelectedListener(this);
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
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}