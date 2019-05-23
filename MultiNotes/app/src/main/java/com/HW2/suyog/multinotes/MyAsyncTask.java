package com.HW2.suyog.multinotes;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private MainActivity mainActivity;

    public MyAsyncTask(MainActivity main) {
        mainActivity = main;
    }

    @Override
    protected String doInBackground(String... strings) {
        return readJsonFileFromDevice();
    }
    private String readJsonFileFromDevice() {
        try {
            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (FileNotFoundException e) {
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(String s) {
        ArrayList<Note> notesList = parseJSON(s);
        mainActivity.update(notesList);
    }

    private ArrayList<Note> parseJSON(String s) {
        ArrayList<Note> noteList = new ArrayList<>();

        try {
            JSONArray list = new JSONArray(s);
            JSONArray properJArray = new JSONArray();
            List<JSONObject> jsonObjList = new ArrayList<JSONObject>();
            for (int i = 0; i < list.length(); i++) {
                jsonObjList.add(list.getJSONObject(i));
            }

            Collections.sort( jsonObjList, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "datetime";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String one = new String();
                    String two = new String();

                    try {
                        one = (String) a.get(KEY_NAME);
                        two = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                    }
                    return -one.compareTo(two);
                }
            });

            for (int i = 0; i < list.length(); i++) {
                properJArray.put(jsonObjList.get(i));
            }

            for (int i = 0; i < properJArray.length(); i++) {
                JSONObject jNotes = (JSONObject) properJArray.get(i);
                String title = jNotes.getString("title");
                String datetime = jNotes.getString("datetime");
                String note = jNotes.getString("desc");
                noteList.add(new Note(title, datetime, note));
            }

            return noteList;
        } catch (Exception e) { return null; }
    }
}
