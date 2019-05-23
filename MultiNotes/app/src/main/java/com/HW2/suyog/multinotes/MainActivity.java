package com.HW2.suyog.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final int B_REQUEST_CODE = 1;
    private NotesAdaptor notesAdaptor;
    private RecyclerView recyclerView;
    private List<Note> notesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycleList);

        notesAdaptor = new NotesAdaptor(notesList, this);

        recyclerView.addItemDecoration(new VerticalSpaceID());

        recyclerView.setAdapter(notesAdaptor);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filegeneration();
        new MyAsyncTask(this).execute();
    }

    private void filegeneration() {
        File file = this.getFileStreamPath(getString(R.string.file_name));
        if(!file.exists()) {
            try {
                FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(file_out_stream);
                writer.write("[]");
                writer.close();
            }
            catch (Exception e) { }
        }
    }

    public void update(ArrayList<Note> cList) {
        if(cList == null) return;
        notesAdaptor.updateList(cList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addIcon:
                Intent intent = new Intent(this,EditActivity.class);
                intent.putExtra("", -1);
                startActivityForResult(intent, B_REQUEST_CODE);
                return true;
            case R.id.helpIcon:
                Intent in = new Intent(this,AboutApp.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonFile();
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Note c = notesList.get(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("", pos);
        intent.putExtra(Note.class.getName(), c);
        //intent.putExtra(Note.class.getName(),c);
        startActivityForResult(intent, B_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        filegeneration();
        new MyAsyncTask(this).execute();

    }
    @Override
    public boolean onLongClick(View view) {
        final int position = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(position);
                notesAdaptor.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
              //do nothing on cancel
            }
        });
        builder.setMessage("Delete Note '"+notesList.get(position).getTitle()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == B_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("USER_MESSAGE_IDENTITY")){
                    Toast.makeText(this,"Un-titled activity was not saved",Toast.LENGTH_SHORT).show();
                }else{
                    String title = data.getStringExtra("USER_TITLE_IDENTITY");
                    String datetime = data.getStringExtra("USER_DATE_IDENTITY");
                    String note = data.getStringExtra("USER_DESC_IDENTITY");
                    int position = data.getIntExtra("NOTE_INDEX", -1);



                    if(position != -1 && notesList.size() > 0) {
                        Note newNotes = notesList.get(position);
                        newNotes.setTitle(title);
                        newNotes.setDate(datetime);
                        newNotes.setDescription(note);
                    }
                    else notesList.add(new Note(title, datetime, note));
                    writeJsonFile();
                }

            }
        }
    }

    private void writeJsonFile() {
        try {
            FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(file_out_stream, getString(R.string.encoding)));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            for (Note n: this.notesList) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(n.getTitle());
                jsonWriter.name("datetime").value(n.getDate());
                jsonWriter.name("desc").value(n.getDescription());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.close();
        } catch (Exception e) {

        }
    }



}
