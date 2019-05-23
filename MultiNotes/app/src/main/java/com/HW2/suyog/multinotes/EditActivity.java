package com.HW2.suyog.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    EditText noteTitle, noteDesc;
    private String date;
    private MainActivity mainActivity;
    private Note note;
    private int location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteDesc = (EditText) findViewById(R.id.noteDesc);

        Intent intent = getIntent();
        if (intent.hasExtra(Note.class.getName())) {
            note = (Note) intent.getSerializableExtra(Note.class.getName());
            noteTitle.setText(note.getTitle());
            noteDesc.setText(note.getDescription());
            date = (String) intent.getSerializableExtra(note.getDate());
        }
        location = (Integer) intent.getSerializableExtra("");

    }

    public String getDateInFormat() {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("EEE MMM dd, h:mm:ss a", Locale.US);
        return sd.format(date);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveIcon:
                Intent data = new Intent();
                if (noteTitle.getText().toString().trim().equals("")){
                    data.putExtra("USER_MESSAGE_IDENTITY", "Un-titled activity was not saved");
                }else{
                    if (note != null && note.getTitle().equals(noteTitle.getText().toString()) && note.getDescription().equals(noteDesc.getText().toString())){
                        data.putExtra("USER_DATE_IDENTITY", note.getDate());
                    }else{ data.putExtra("USER_DATE_IDENTITY", getDateInFormat()); }

                    data.putExtra("USER_TITLE_IDENTITY", noteTitle.getText().toString());
                    data.putExtra("USER_DESC_IDENTITY", noteDesc.getText().toString());
                }
                data.putExtra("NOTE_INDEX", location);
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if(note != null && (!(note.getTitle().equals(noteTitle.getText().toString())) || !(note.getDescription().equals(noteDesc.getText().toString())))){
            displayDialogBox();
        }else if (note == null && (noteTitle.getText().toString().length() > 0 || noteDesc.getText().toString().length() > 0)){
            displayDialogBox();
        }else if(note == null && (noteTitle.getText().toString().length()==0 && noteDesc.getText().toString().length() == 0))
        {
            displayDialogBox();
        }else{
            super.onBackPressed();
        }
    }

    public void displayDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            Intent data = new Intent();
            if (noteTitle.getText().toString().length() == 0) {
                finish();
                Toast.makeText(EditActivity.this,"Your note is not saved as Title is missing!.",Toast.LENGTH_SHORT).show();
            }
            else {
                if (note != null && note.getTitle().equals(noteTitle.getText().toString()) && note.getDescription().equals(noteDesc.getText().toString())) {
                data.putExtra("USER_DATE_IDENTITY", note.getDate());
            } else {
                data.putExtra("USER_DATE_IDENTITY", getDateInFormat());
            }
            data.putExtra("USER_TITLE_IDENTITY", noteTitle.getText().toString());
            data.putExtra("USER_DESC_IDENTITY", noteDesc.getText().toString());
            data.putExtra("NOTE_INDEX", location);
            setResult(RESULT_OK, data);
            finish();
            }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                finish(); }
        });

        builder.setMessage("Note is not saved! Please save note '"+noteTitle.getText().toString()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
