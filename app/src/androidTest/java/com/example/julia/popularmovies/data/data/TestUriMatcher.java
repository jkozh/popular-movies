package com.example.julia.popularmovies.data.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.julia.popularmovies.data.MovieContract;
import com.example.julia.popularmovies.data.MovieProvider;

public class TestUriMatcher extends AndroidTestCase {

    // "content://com.example.julia.popularmovies/movie"
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;

    // This function tests that your UriMatcher returns the correct integer value
    // for each of the Uri types that our ContentProvider can handle.
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();
        assertEquals("Error: The MOVIE URI was matched incorrectly.",
            testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
    }
}
