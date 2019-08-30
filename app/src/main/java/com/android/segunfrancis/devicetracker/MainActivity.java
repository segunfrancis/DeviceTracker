package com.android.segunfrancis.devicetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView startLongitude;
    private TextView startLatitude;
    private TextView endLongitude;
    private TextView endLatitude;
    private TextView totalDistance;
    private Button startButton;
    private Button stopButton;
    private Button saveButton;
    private boolean gpsStatus = false;

    private Location mLocation;
    private LocationManager mLocationManager;
    private Context mContext;
    private Criteria mCriteria;
    private String holder;
    private static final String TAG = "location_change";
    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableRuntimePermission();

        startLatitude = findViewById(R.id.start_latitude_text);
        startLongitude = findViewById(R.id.start_longitude_text);
        endLatitude = findViewById(R.id.end_latitude_text);
        endLongitude = findViewById(R.id.end_longitude_text);
        totalDistance = findViewById(R.id.total_distance_text);

        startButton = findViewById(R.id.startTracking);
        stopButton = findViewById(R.id.stopTracking);
        saveButton = findViewById(R.id.saveButton);

        stopButton.setEnabled(false);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCriteria = new Criteria();
        holder = mLocationManager.getBestProvider(mCriteria, false);
        mContext = getApplicationContext();
        checkGpsStatus();

        // Start distance tracking
        startButton.setOnClickListener(view -> {
            checkGpsStatus();

            if (gpsStatus) {
                if (holder != null) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    // Registering location Listener
                    mLocationManager.requestLocationUpdates(holder, 0, 0, MainActivity.this);

                    mLocation = mLocationManager.getLastKnownLocation(holder);
                    Log.d(TAG, "onClick: " + mLocation);

                    startLatitude.setText(String.valueOf(mLocation.getLatitude()));
                    startLongitude.setText(String.valueOf(mLocation.getLongitude()));
                    stopButton.setEnabled(true);
                    startButton.setEnabled(false);
                }
            } else {
                Toast.makeText(MainActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Stop distance tracking
        stopButton.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocation = mLocationManager.getLastKnownLocation(holder);
            Log.d(TAG, "onClick: " + mLocation);
            endLongitude.setText(String.valueOf(mLocation.getLongitude()));
            endLatitude.setText(String.valueOf(mLocation.getLatitude()));
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            saveButton.setVisibility(View.VISIBLE);

            // Convert longitude and latitude strings back to floats
            double startLongitudeFloat = Double.parseDouble(startLongitude.getText().toString());
            double startLatitudeFloat = Double.parseDouble(startLatitude.getText().toString());
            double endLongitudeFloat = Double.parseDouble(endLongitude.getText().toString());
            double endLatitudeFloat = Double.parseDouble(endLatitude.getText().toString());
            double estimatedDistance = getDistanceBetweenTwoPoints(startLongitudeFloat, startLatitudeFloat, endLongitudeFloat, endLatitudeFloat);
            totalDistance.setText(String.valueOf(estimatedDistance));

            // Remove updates
            mLocationManager.removeUpdates(MainActivity.this);
        });

        // Save distance covered
        saveButton.setOnClickListener(view -> {
            String distance = totalDistance.getText().toString();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            String time = new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime());
            Distance distanceObject = new Distance(distance, date, time);
            Intent intent = new Intent(MainActivity.this, DistanceActivity.class);
            intent.putExtra(DistanceActivity.VALUES, distanceObject);
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

    private void enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(MainActivity.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
        }
    }

    public void checkGpsStatus() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        gpsStatus = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove updates
        mLocationManager.removeUpdates(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.distance_list) {
            Intent intent = new Intent(MainActivity.this, DistanceActivity.class);
            startActivity(intent);
        } else if (id == R.id.reset) {
            clearFields();
        }
        return super.onOptionsItemSelected(item);
    }

    // Calculating the distance covered by the device
    private double getDistanceBetweenTwoPoints(double longStart, double latStart, double longEnd, double latEnd) {
        int radiusOfEarth = 6371000; // meters
        double dLat = Math.toRadians(latEnd - latStart);
        double dLong = Math.toRadians(longEnd - longStart);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latStart))
                * Math.cos(Math.toRadians(latEnd))
                * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double meter = Double.valueOf(newFormat.format(radiusOfEarth * c));
        Log.d(TAG, "getDistanceBetweenTwoPoints: " + meter);
        return meter;
    }

    // Clearing all the values in the text views and
    // restoring the original state of the buttons
    private void clearFields() {
        startLongitude.setText("");
        startLatitude.setText("");
        endLongitude.setText("");
        endLatitude.setText("");
        totalDistance.setText("");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        saveButton.setVisibility(View.GONE);
    }
}
