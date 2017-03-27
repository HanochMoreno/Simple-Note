package com.example.hanoch.simplenote.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.hanoch.simplenote.Note;

public class NotesProviderHandler {

    Context context;

//-------------------------------------------------------------------------------------------------

    public NotesProviderHandler(Context context) {
        this.context = context;
    }

//-------------------------------------------------------------------------------------------------

    public Uri addNewNote(Note note) {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        ContentValues values = new ContentValues();

        values.put(NotesContract.Notes.TITLE, note.getTitle());
        values.put(NotesContract.Notes.TIME, note.getLastUpdate());
        values.put(NotesContract.Notes.ITEMS_LIST, note.getItemsList());

        return context.getContentResolver().insert(contentUri, values);
    }

//-------------------------------------------------------------------------------------------------

    public long getNoteIdFromUri(Uri uri) {

        String noteId = uri.getLastPathSegment();
        long id = Long.parseLong(noteId);

        return id;
    }

//-------------------------------------------------------------------------------------------------

    public int updateNote(Note note) {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        String selection = NotesContract.Notes._ID + "=?";
        String[] selectionArgs = {note.getId() + ""};

        ContentValues values = new ContentValues();

        values.put(NotesContract.Notes.TITLE, note.getTitle());
        values.put(NotesContract.Notes.TIME, note.getLastUpdate());
        values.put(NotesContract.Notes.ITEMS_LIST, note.getItemsList());

        return context.getContentResolver().update(contentUri, values, selection, selectionArgs);
    }

//-------------------------------------------------------------------------------------------------

    public int deleteNoteById(long id) {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        String selection = NotesContract.Notes._ID + "=?";
        String[] selectionArgs = {id + ""};

        return context.getContentResolver().delete(contentUri, selection, selectionArgs);
    }

//-------------------------------------------------------------------------------------------------

    public int deleteAllNotes() {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        String selection = null;
        String[] selectionArgs = null;

        return context.getContentResolver().delete(contentUri, selection, selectionArgs);
    }

//-------------------------------------------------------------------------------------------------

    public Cursor queryNoteCursorById(long id) {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        // get all columns (projection = null)
        String[] projection = null;

        // get the row with id= id
        String selection = NotesContract.Notes._ID + "=?";
        String[] selectionArgs = {id + ""};

        String sortOrder = NotesContract.Notes._ID + " ASC";

        return context.getContentResolver().query(contentUri, projection, selection, selectionArgs, sortOrder);
    }

//-------------------------------------------------------------------------------------------------

    public Note queryNoteObjectById(long id) {

        Cursor cursor = queryNoteCursorById(id);
        cursor.moveToNext();

        String title = cursor.getString(cursor.getColumnIndex(NotesContract.Notes.TITLE));
        String time = cursor.getString(cursor.getColumnIndex(NotesContract.Notes.TIME));
        String itemsList = cursor.getString(cursor.getColumnIndex(NotesContract.Notes.ITEMS_LIST));

        cursor.close();

        return new Note(id, title, itemsList, time);
    }

//-------------------------------------------------------------------------------------------------

    public Cursor queryNotesByTitle(String title) {

        Uri contentUri = NotesContract.Notes.CONTENT_URI;

        // get all columns (projection = null)
        String[] projection = null;

        // get the row with title= title
        String selection = NotesContract.Notes.TITLE + " LIKE ? ";
        String[] selectionArgs = {"%" + title + "%"};

        String sortOrder = NotesContract.Notes.TITLE + " COLLATE LOCALIZED ASC";

        return context.getContentResolver().query(contentUri, projection, selection, selectionArgs, sortOrder);
    }

}
