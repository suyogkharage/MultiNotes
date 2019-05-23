package com.HW2.suyog.multinotes;

import java.io.Serializable;

public class Note implements Serializable {

    private String noteTitle;
    private String description;
    private String dateTime;

    public Note(String noteTitle,  String dateTime,String description) {
        this.noteTitle = noteTitle;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return noteTitle;
    }

    public void setTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}
