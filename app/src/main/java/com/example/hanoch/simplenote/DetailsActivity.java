package com.example.hanoch.simplenote;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hanoch.simplenote.database.NotesProviderHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {

    public final static String LIST_DIVIDER = "&&";
    public final static String NEW_NOTE = "!!!!!!";

    private long noteId;
    private FloatingActionButton fab_addNoteItem;
    private NotesProviderHandler nph;
    private ListView listView_noteItems;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> noteItemsList;
    private String noteTitle;
    private Button button_saveNote;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorBrown));
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditTitleDialog(noteTitle);
                return true;
            }
        });
        TextView textView_time = (TextView) findViewById(R.id.textView_time);

        Intent i = getIntent();
        noteId = i.getLongExtra(MainActivity.EXTRA_NOTE_ID, -1);

        nph = new NotesProviderHandler(this);

        if (noteId == -1) {
            // Creating a new note
            noteTitle = "";
            noteItemsList = new ArrayList<String>();
            toolbar.setTitle(R.string.long_click_edit_title);
            textView_time.setVisibility(View.GONE);

        } else {
            // Editing an existing note
            Note note = nph.queryNoteObjectById(noteId);
            noteTitle = note.getTitle();
            noteItemsList = stringToListConverter(note.getItemsList());
            toolbar.setTitle(note.getTitle());
            textView_time.setText(note.getLastUpdate());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, noteItemsList);

        listView_noteItems = (ListView) findViewById(R.id.listView_noteItems);
        listView_noteItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                view.findViewById(android.R.id.text1)

                String itemText = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                showEditItemDialog(itemText, position);
                adapter.notifyDataSetChanged();
            }
        });

        listView_noteItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteItemDialog(position);
                return true;
            }
        });
        listView_noteItems.setAdapter(adapter);

        button_saveNote = (Button) findViewById(R.id.button_save);
        button_saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveNoteClick();
                finish();
            }
        });

        fab_addNoteItem = (FloatingActionButton) findViewById(R.id.fab_addNoteItem);
        fab_addNoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditItemDialog(NEW_NOTE, -1);
            }
        });
    }

//-------------------------------------------------------------------------------------------------

    private void onSaveNoteClick() {

        String stringItemsList = listToStringConverter(noteItemsList);

        if (noteTitle.equals("")) noteTitle = getString(R.string.new_note);

        Note note = new Note(noteId, noteTitle, stringItemsList, getStringTimeStamp());

        if (noteId == -1) { // Adding a new note
            nph.addNewNote(note);

        } else { // Saving an edited note
            nph.updateNote(note);
        }
    }

//-------------------------------------------------------------------------------------------------

    private void showEditTitleDialog(final String titleText) {
        // pop-up an alertDialog with the note details to edit or adding a new item
        // save it back to the list when "save" button clicked

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_edit_item, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        final EditText editText_userInput = (EditText) dialog.findViewById(R.id.editText_userInput);
        editText_userInput.setText(titleText.equals(NEW_NOTE) ? "" : titleText);

        Button button_save = (Button) dialog.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the input text and add it to the list
                noteTitle = editText_userInput.getText().toString();

                if (noteTitle.equals("")) {
                    toolbar.setTitle(R.string.long_click_edit_title);
                } else {
                    toolbar.setTitle(noteTitle);
                }

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        // Display the dialog
        dialog.show();
    }

//-------------------------------------------------------------------------------------------------

    private void showEditItemDialog(final String itemText, final int position) {
        // pop-up an alertDialog with the note details to edit or adding a new item
        // save it back to the list when "save" button clicked

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_edit_item, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        final EditText editText_userInput = (EditText) dialog.findViewById(R.id.editText_userInput);
        editText_userInput.setText(itemText.equals(NEW_NOTE) ? "" : itemText);

        Button button_save = (Button) dialog.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the input text and add it to the list
                String item = editText_userInput.getText().toString();

                if (itemText.equals(NEW_NOTE)) {
                    noteItemsList.add(item);
                } else {
                    noteItemsList.set(position, item);
                }

                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

//-------------------------------------------------------------------------------------------------

    private void showDeleteItemDialog(final int position) {
        // pop-up an alertDialog with the note details to edit or adding a new item
        // save it back to the list when "save" button clicked

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // inflate the layout dialog_layout.xml and set it as contentView
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_warning, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        TextView textView_dialogTitle = (TextView) dialog.findViewById(R.id.textView_dialogTitle);
        TextView textView_dialogContent = (TextView) dialog.findViewById(R.id.textView_dialogContent);

        textView_dialogTitle.setText(R.string.delete_note);
        textView_dialogContent.setText(getString(R.string.are_you_sure)
                + "\n"
                + getString(R.string.the_operation_is_irreversible));

        Button button_save = (Button) dialog.findViewById(R.id.button_yes);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteItemsList.remove(position);
                adapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        Button button_cancel = (Button) dialog.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dialog.dismiss();
            }
        });

        // Display the dialog
        dialog.show();
    }

//-------------------------------------------------------------------------------------------------

    public static String listToStringConverter(ArrayList<String> list) {

        String convertedList = "";

        for (String item : list) {

            if (!item.trim().equals("")) convertedList = convertedList + item + LIST_DIVIDER;
        }

        return convertedList;
    }

//-------------------------------------------------------------------------------------------------

    public static ArrayList<String> stringToListConverter(String convertedList) {

        ArrayList<String> resultList = new ArrayList<>();

        String[] splitString = (convertedList.split(LIST_DIVIDER));

        for (String aSplitString : splitString) {

            if (!aSplitString.equals("")) resultList.add(aSplitString);
        }

        return resultList;
    }

//-------------------------------------------------------------------------------------------------

    private String getStringTimeStamp() {

        Calendar cal = Calendar.getInstance();

//        12 hours format
//        int hour = cal.get(Calendar.HOUR);

        // 24 hours format
        String hour = (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + cal.get(Calendar.HOUR_OF_DAY) : cal.get(Calendar.HOUR_OF_DAY) + "");
        String minute = (cal.get(Calendar.MINUTE) < 10 ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE) + "");
        String second = (cal.get(Calendar.SECOND) < 10 ? "0" + cal.get(Calendar.SECOND) : cal.get(Calendar.SECOND) + "");
        String dayOfMonth = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH) + "");
        String month = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "");
        String year = cal.get(Calendar.YEAR) + "";

        return (dayOfMonth + "/" + month + "/" + year + "  ,  " + hour + ":" + minute + ":" + second);
    }
}
