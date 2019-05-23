package com.HW2.suyog.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotesHolder extends RecyclerView.ViewHolder{
    protected TextView title;
    protected TextView desc;
    protected TextView date;

    public NotesHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        date = (TextView) itemView.findViewById(R.id.date);
        desc = (TextView) itemView.findViewById(R.id.desc);


    }
}
