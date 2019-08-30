package com.android.segunfrancis.devicetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DistanceActivity extends AppCompatActivity {

    public static final String VALUES = "values";
    private DistanceViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        Intent intent = getIntent();
        Distance distance = (Distance) intent.getSerializableExtra(VALUES);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        DistanceAdapter adapter = new DistanceAdapter(DistanceActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewModel = ViewModelProviders.of(this).get(DistanceViewModel.class);
        mViewModel.getAllDistances().observe(this, adapter::setDistances);
        if (distance != null) {
            mViewModel.insert(distance);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            confirmDelete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.confirm_delete_message));
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            mViewModel.deleteAllEntries();
            dialogInterface.dismiss();
            Toast.makeText(DistanceActivity.this, "Deleting...", Toast.LENGTH_SHORT).show();
            finish();
        }).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
