package com.example.julia.popularmovies.data.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.julia.popularmovies.data.MovieContract;

import java.util.Map;
import java.util.Set;

// TODO: change deprecated class
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "123");
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Capitan America");
        movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, "2016-08-12");
        movieValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "Synopsis example");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, "poster URL");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, "7.5");
        return movieValues;
    }
}
