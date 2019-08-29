package com.android.segunfrancis.devicetracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Distance.class}, version = 1)
public abstract class DistanceRoomDatabase extends RoomDatabase {
    public abstract DistanceDao distanceDao();

    private static volatile DistanceRoomDatabase INSTANCE;

    public static DistanceRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (DistanceRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DistanceRoomDatabase.class, "distance_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
