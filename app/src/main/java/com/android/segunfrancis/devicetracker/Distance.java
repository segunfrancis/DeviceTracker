package com.android.segunfrancis.devicetracker;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Distance {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String distance;

    public Distance(int id, String distance) {
        this.id = id;
        this.distance = distance;
    }

    @Ignore
    public Distance(String distance) {
        this.distance = distance;
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
}
