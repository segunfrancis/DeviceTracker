package com.android.segunfrancis.devicetracker;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DistanceRepository {
    private DistanceDao mDistanceDao;
    private LiveData<List<Distance>> mData;

    public DistanceRepository(Application application) {
        DistanceRoomDatabase db = DistanceRoomDatabase.getDatabase(application);
        mDistanceDao = db.distanceDao();
        mData = mDistanceDao.getAllDistances();
    }

    public LiveData<List<Distance>> getAllDistances() {
        return mData;
    }

    public void insert(Distance distance) {
        new insertAsyncTask(mDistanceDao).execute(distance);
    }

    public static class insertAsyncTask extends AsyncTask<Distance, Void, Void> {
        private DistanceDao mDistanceDao;

        public insertAsyncTask(DistanceDao distanceDao) {
            this.mDistanceDao = distanceDao;
        }

        @Override
        protected Void doInBackground(Distance... distances) {
            mDistanceDao.insert(distances[0]);
            return null;
        }
    }
}
