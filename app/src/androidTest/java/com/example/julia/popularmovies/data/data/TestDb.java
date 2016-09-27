package com.example.julia.popularmovies.data.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.julia.popularmovies.data.MovieContract;
import com.example.julia.popularmovies.data.MovieDbHelper;

public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    private void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
       This function gets called before each test is executed to delete the database.  This makes
       sure that we always have a clean test.
    */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
       A code to test that we can insert and query the database.
    */
    public void testMovieTable() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step : Create movie values
        ContentValues movieValues = TestUtilities.createMovieValues();

        // Third Step : Insert ContentValues into database and get a row ID back
        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);
        assertTrue(movieRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from query", cursor.moveToFirst() );

        // Fifth Step: Validate the  Query
        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate",
                cursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );

        // Sixth Step: Close cursor and database
        cursor.close();
        db.close();
        dbHelper.close();
    }

}
