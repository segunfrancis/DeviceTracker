package com.android.segunfrancis.devicetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView locationText;
    private ProgressBar pb;
    private Button startButton;
    private boolean flag = false;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final String TAG = "location_change";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.location_text);
        pb = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startTracking);

        pb.setVisibility(View.GONE);

        startButton.setOnClickListener(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View view) {
        flag = displayGPSStatus();
        if (flag) {
            locationText.setText("Move your device");
            pb.setVisibility(View.VISIBLE);
            mLocationListener = new MyLocationListener();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, mLocationListener);
        } else {
            alertBox("GPS status: ", "OFF");
        }
    }

    private void alertBox(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS Disabled")
                .setCancelable(false)
                .setPositiveButton("ON GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO:
                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivity(intent);
                        dialogInterface.cancel();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean displayGPSStatus() {
        ContentResolver resolver = getBaseContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            locationText.setText("");

            Toast.makeText(getBaseContext(), "Location changed: Lat: " + location.getLatitude() + "Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            String coordinates = "Longitude: " + location.getLongitude() + "\n" + "Latitude: " + location.getLatitude();
            Log.d(TAG, "onLocationChanged: " + coordinates);
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
    }
}
