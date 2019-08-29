package com.android.segunfrancis.devicetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView startLongitude;
    private TextView startLatitude;
    private TextView endLongitude;
    private TextView endLatitude;
    private Group mGroup;
    private Button startButton;
    private Button stopButton;
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

        mGroup = findViewById(R.id.group);
        startButton = findViewById(R.id.startTracking);
        stopButton = findViewById(R.id.stopTracking);

        stopButton.setEnabled(false);

        mGroup.setVisibility(View.GONE);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGpsStatus();

                if (gpsStatus) {
                    if (holder != null) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mLocation = mLocationManager.getLastKnownLocation(holder);
                        Log.d(TAG, "onClick: " + mLocation);
                        startLongitude.setText(String.valueOf(mLocation.getLongitude()));
                        startLatitude.setText(String.valueOf(mLocation.getLatitude()));

                        // Registering location Listener
                        mLocationManager.requestLocationUpdates(holder, 0, 0, MainActivity.this);
                        stopButton.setEnabled(true);
                        startButton.setEnabled(false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCriteria = new Criteria();
        holder = mLocationManager.getBestProvider(mCriteria, false);
        mContext = getApplicationContext();

        checkGpsStatus();
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
/*        locationText.setText("");
        mGroup.setVisibility(View.GONE);

//        Toast.makeText(getBaseContext(), "Location changed:" + "\n" + "Lat: " + location.getLatitude() + "\n" +  " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        String coordinates = "Longitude: " + location.getLongitude() + "\n" + "Latitude: " + location.getLatitude();
        locationText.setText(coordinates);
        Log.d(TAG, "onLocationChanged: " + coordinates);*/
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
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
