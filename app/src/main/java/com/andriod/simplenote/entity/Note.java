package com.andriod.simplenote.entity;

import java.util.Calendar;
import java.util.UUID;

public class Note {

    private final String id;
    private String header;
    private long date;


    private boolean favorite;

    public Note(){
        this(null, getCurrentDate());
    }

    public Note(String header, long date) {
        this.id = UUID.randomUUID().toString();
        this.header = header;
        this.date = date;
    }

    private static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
