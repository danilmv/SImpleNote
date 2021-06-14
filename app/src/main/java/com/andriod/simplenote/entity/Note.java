package com.andriod.simplenote.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.UUID;

public class Note implements Parcelable {

    private final String id;
    private String header;
    private long date;

    public Note(){
        this(null, getCurrentDate());
    }

    public Note(String header, long date) {
        this.id = UUID.randomUUID().toString();
        this.header = header;
        this.date = date;
    }

    protected Note(Parcel in) {
        id = in.readString();
        header = in.readString();
        date = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(header);
        dest.writeLong(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
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
