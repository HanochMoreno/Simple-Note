package com.example.hanoch.simplenote;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanoch.simplenote.database.NotesContract;

import java.util.Random;

/**
 * Created by Hanoch on 07-Mar-16.
 */
public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(NotesContract.Notes.TITLE));

//        ImageView imageView_note = (ImageView) view.findViewById(R.id.imageView_note);
        TextView textView_title = (TextView) view.findViewById(R.id.textView_title);

        int colorArrayPosition = cursor.getPosition();

        while (colorArrayPosition >= AppConsts.colorsArray.length) {
            colorArrayPosition -= AppConsts.colorsArray.length;
        }

        textView_title.setBackgroundColor(ContextCompat.getColor(context, AppConsts.colorsArray[colorArrayPosition]));
        float rotation = getRandomInt(-15, 15);
        view.setRotation(rotation);

        textView_title.setText(title);
    }

    private int getRandomInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
