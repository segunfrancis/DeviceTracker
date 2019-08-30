package com.android.segunfrancis.devicetracker;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DistanceDao {

    @Insert
    void insert(Distance distance);

    @Query("SELECT * from distance_table ORDER BY id DESC")
    LiveData<List<Distance>> getAllDistances();

    @Query("DELETE from distance_table")
    void deleteAllEntries();
}
