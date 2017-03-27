package com.example.hanoch.simplenote.database;

import android.net.Uri;

public class NotesContract {

    public static final String AUTHORITY = "com.hanoch.notesdbprovider.provider.notes";

//-------------------------------------------------------------------------------------------------

    public static class Notes {

        public static final String TABLE_NAME  = "notes";
        public static final Uri CONTENT_URI  = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);
        public static final String _ID  = "_id";
        public static final String TITLE = "title";
        public static final String TIME = "time";
        public static final String ITEMS_LIST = "itemsList";
    }

}
