package com.HW2.suyog.multinotes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdaptor extends RecyclerView.Adapter<NotesHolder>{
    private List<Note> notesList;
    private MainActivity mainActivity;

    public NotesAdaptor(List<Note> notesList, MainActivity main) {
        this.notesList = notesList;
        this.mainActivity = main;
    }

    public void updateList(List<Note> list) {
        this.notesList.clear();
        this.notesList.addAll(list);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteview, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NotesHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull NotesHolder Holder, int pos) {
        Holder.title.setText(notesList.get(pos).getTitle());

        SimpleDateFormat SDF1 = new SimpleDateFormat("EEE MMM dd, h:mm a", Locale.US);
        SimpleDateFormat SDF2 = new SimpleDateFormat("EEE MMM dd, h:mm:ss a", Locale.US);

        String datetime = new String();

        try {
            Date date = SDF2.parse(notesList.get(pos).getDate());
            datetime = SDF1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Holder.date.setText(datetime);


        String descLength = notesList.get(pos).getDescription();
        if(descLength.length()>80){
            descLength  =  descLength.substring(0,79)+"...";
        }
        Holder.desc.setText(descLength);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
