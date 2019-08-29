package com.android.segunfrancis.devicetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class DistanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        Intent intent = getIntent();
        Distance distance = (Distance) intent.getSerializableExtra("values");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        DistanceAdapter adapter = new DistanceAdapter(DistanceActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DistanceViewModel viewModel = ViewModelProviders.of(this).get(DistanceViewModel.class);
        viewModel.getAllDistances().observe(this, adapter::setDistances);
        if (distance != null) {
            viewModel.insert(distance);
        }
    }
}
