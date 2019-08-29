package com.android.segunfrancis.devicetracker;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "distance_table")
public class Distance {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String distance;
    private String date;
    private String time;

    public Distance(int id, String distance, String date, String time) {
        this.id = id;
        this.distance = distance;
        this.date = date;
        this.time = time;
    }

    @Ignore
    public Distance(String distance, String date, String time) {
        this.distance = distance;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
