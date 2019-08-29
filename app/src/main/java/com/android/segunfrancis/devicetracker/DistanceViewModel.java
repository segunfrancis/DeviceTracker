package com.android.segunfrancis.devicetracker;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DistanceViewModel extends AndroidViewModel {
    private DistanceRepository mRepository;
    LiveData<List<Distance>> mData;

    public DistanceViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DistanceRepository(application);
        mData = mRepository.getAllDistances();
    }

    public LiveData<List<Distance>> getAllDistances() {
        return mData;
    }

    public void insert(Distance distance) {
        mRepository.insert(distance);
    }
}
