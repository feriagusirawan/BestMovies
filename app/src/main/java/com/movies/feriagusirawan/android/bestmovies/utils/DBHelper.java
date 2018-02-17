package com.movies.feriagusirawan.android.bestmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_BACKDROP;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_DATE;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_ID;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_MOV_ID;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_MOV_NAME;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_OVERVIEW;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_POSTER;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.COL_VOTE;
import static com.movies.feriagusirawan.android.bestmovies.utils.MoviesContract.MoviesEntry.TABLE_NAME;


public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DB_NAME = "fav_movies.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String STREAM_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MOV_ID + " TEXT, " + COL_POSTER + " TEXT, "
                + COL_BACKDROP + " TEXT, " + COL_MOV_NAME + " TEXT, "
                + COL_DATE + " TEXT, " + COL_VOTE + " TEXT, "
                + COL_OVERVIEW + " TEXT" + ")";
        db.execSQL(STREAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertRow(String poster, String backdrop, String movId, String movName,
                             String date, String vote, String overview) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_POSTER, poster);
        values.put(COL_BACKDROP, backdrop);
        values.put(COL_MOV_ID, movId);
        values.put(COL_MOV_NAME, movName);
        values.put(COL_DATE, date);
        values.put(COL_VOTE, vote);
        values.put(COL_OVERVIEW, overview);
        long num = db.insert(TABLE_NAME, null, values);
        if (num == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteRow(String movId) {
        db = getWritableDatabase();
        db.delete(TABLE_NAME, COL_MOV_ID + " =?", new String[]{movId});
    }

    public boolean ifExist(String movId) {
        db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_MOV_ID}, COL_MOV_ID + " =?",
                new String[]{movId}, null, null, null);
        if (cursor != null && cursor.moveToNext()) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getData() {
        db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
