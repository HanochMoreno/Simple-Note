package com.example.hanoch.simplenote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "RecipesList.db";
    private static final int DB_VERSION = 1;

    public NotesDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating an SQL database file.
        // happens only at the first time the application is opened.
        String sql = "CREATE TABLE " + NotesContract.Notes.TABLE_NAME + "(" +
                NotesContract.Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NotesContract.Notes.TITLE + " TEXT," +
                NotesContract.Notes.TIME + "  TEXT," +
                NotesContract.Notes.ITEMS_LIST + " TEXT" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // defining what to do when we want to update the database
        // for example, split the column "name" into 2 columns: "first name" and "last name"

        // in our case we will just remove the old db table and create a new one...

        String sql;

        sql = "DROP TABLE IF EXISTS " + NotesContract.Notes.TABLE_NAME;
        db.execSQL(sql);

        onCreate(db);
    }
}
