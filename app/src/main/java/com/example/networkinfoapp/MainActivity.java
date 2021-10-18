package com.example.networkinfoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        Button add = findViewById(R.id.Add);
        Button check = findViewById(R.id.check);
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                MainActivity.this);
        String[] wifiInfo = {"Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check"};
        String[] mobileInfo = {"Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check", "Check"};
        check.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                String connection;
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    connection = activeNetwork.getTypeName();
                    if (connection.equals("MOBILE")) {
                        mobileInfo[0] = connection;
                        boolean permissionGranted = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                        if (!permissionGranted)
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                        permissionGranted = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                        if (!permissionGranted)
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        List<CellInfo> cellInfoList = tm.getAllCellInfo();
                        for (int i = 0; i < cellInfoList.size(); i++) {
                            CellInfo cellInfo = (CellInfo) cellInfoList.get(i);
                            int cellID, cellMcc, cellMnc, cellPci, cellTac, rsrp, maxDownloadSpeed, maxUploadSpeed;
                            if (cellInfo instanceof CellInfoLte && cellInfo.isRegistered()) {
                                cellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                                mobileInfo[1] = String.valueOf(cellID);
                                // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                                cellMcc = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();
                                mobileInfo[2] = String.valueOf(cellMcc);
                                // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                                cellMnc = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();
                                mobileInfo[3] = String.valueOf(cellMnc);
                                // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                                cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();
                                mobileInfo[4] = String.valueOf(cellPci);
                                // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                                cellTac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();
                                mobileInfo[5] = String.valueOf(cellTac);
                                // gets RSRP cell signal strength:
                                rsrp = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                                mobileInfo[6] = rsrp + " dbm";
                                maxDownloadSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
                                mobileInfo[7] = maxDownloadSpeed + " MBPS";
                                maxUploadSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
                                mobileInfo[8] = maxUploadSpeed + " MBPS";
                            }
                            permissionGranted = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                            if (!permissionGranted)
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                            permissionGranted = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                            if (!permissionGranted)
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                            LocationManager locationManager = (LocationManager) getSystemService(
                                    Context.LOCATION_SERVICE
                            );
                            if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) ||
                                    locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
                                //when location service is enabled
                                //get last location
                                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Location> task) {
                                        //initialize location
                                        Location location = task.getResult();
                                        //check condition
                                        if (location != null) {
                                            //when location is not null
                                            //set long and latitude
                                            String latitude = String.valueOf(location.getLatitude());
                                            mobileInfo[9] = latitude;
                                            String longitude = String.valueOf(location.getLongitude());
                                            mobileInfo[10] = longitude;
                                            Log.d("testing", latitude);
                                            Log.d("testing", longitude);
                                        } else {
                                            //when location result is null
                                            // Initialize request
                                            com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                                    .setInterval(10000)
                                                    .setFastestInterval(1000)
                                                    .setNumUpdates(1);
                                            //Initialize location call back
                                            LocationCallback locationCallback = new LocationCallback() {
                                                @Override
                                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                                    //super.onLocationResult(locationResult);
                                                    Location location1 = locationResult.getLastLocation();
                                                    String latitude = String.valueOf(location.getLatitude());
                                                    mobileInfo[9] = latitude;
                                                    String longitude = String.valueOf(location.getLongitude());
                                                    mobileInfo[10] = longitude;
                                                    Log.d("testing", String.valueOf(location1));
                                                    Log.d("testing", latitude);
                                                    Log.d("testing", longitude);
                                                }
                                            };
                                            boolean permissionGranted1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                                            if (!permissionGranted1)
                                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                                            permissionGranted1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                                            if (!permissionGranted1)
                                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                                            LocationManager locationManager = (LocationManager) getSystemService(
                                                    Context.LOCATION_SERVICE);
                                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                                    locationCallback, Looper.myLooper());
                                        }
                                        InfoAdapter wa = new InfoAdapter(MainActivity.this, R.layout.wifi_layout, mobileInfo);
                                        listView.setAdapter(wa);
                                    }
                                });
                            }else{
                                //when location service is not enabled
                                // open location settings
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }

                    }
                    else if(connection.equals("WIFI")){
                        Log.d("testing", connection);
                        wifiInfo[0] = connection;
                        WifiManager wm = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        // Method to get the current connection info
                        WifiInfo wi = wm.getConnectionInfo();

                        // Extracting the information from the received connection info
                        int ip = wi.getIpAddress();
                        wifiInfo[1] = String.valueOf(ip);
                        int linkSpeed = wi.getLinkSpeed();
                        wifiInfo[2] = String.valueOf(linkSpeed);
                        int networkID = wi.getNetworkId();
                        wifiInfo[3] = String.valueOf(networkID);
                        int rssi = wi.getRssi();
                        wifiInfo[4] = rssi + " dbm";
                        String ssid = wi.getSSID();
                        wifiInfo[5] = ssid;
                        boolean hssid = wi.getHiddenSSID();
                        wifiInfo[6] = String.valueOf(hssid);
                        String bssid = wi.getBSSID();
                        wifiInfo[7] = bssid;
                        int maxDownloadSpeed = nc.getLinkDownstreamBandwidthKbps()/1000;
                        wifiInfo[8] = maxDownloadSpeed + " MBPS";
                        int maxUploadSpeed = nc.getLinkUpstreamBandwidthKbps()/1000;
                        wifiInfo[9] = maxUploadSpeed + " MBPS";
                        LocationManager locationManager = (LocationManager) getSystemService(
                                Context.LOCATION_SERVICE
                        );
                        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)){
                            //when location service is enabled
                            //get last location
                            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    //initialize location
                                    Location location = task.getResult();
                                    //check condition
                                    if(location != null){
                                        //when location is not null
                                        //set long and latitude
                                        String latitude = String.valueOf(location.getLatitude());
                                        wifiInfo[10] = latitude;
                                        String longitude = String.valueOf(location.getLongitude());
                                        wifiInfo[11] = longitude;
                                        Log.d("testing", latitude);
                                        Log.d("testing", longitude);
                                    } else{
                                        //when location result is null
                                        // Initialize request
                                        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                                .setInterval(10000)
                                                .setFastestInterval(1000)
                                                .setNumUpdates(1);
                                        //Initialize location call back
                                        LocationCallback locationCallback = new LocationCallback() {
                                            @Override
                                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                                //super.onLocationResult(locationResult);
                                                Location location1 = locationResult.getLastLocation();
                                                String latitude = String.valueOf(location.getLatitude());
                                                wifiInfo[10] = latitude;
                                                String longitude = String.valueOf(location.getLongitude());
                                                wifiInfo[11] = longitude;
                                                Log.d("testing", String.valueOf(location1));
                                                Log.d("testing", latitude);
                                                Log.d("testing", longitude);
                                            }
                                        };
                                        boolean permissionGranted1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                                        if (!permissionGranted1)
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                                        permissionGranted1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                                        if (!permissionGranted1)
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
                                        LocationManager locationManager = (LocationManager) getSystemService(
                                                Context.LOCATION_SERVICE);
                                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                                locationCallback, Looper.myLooper());
                                    }
                                    InfoAdapter wa = new InfoAdapter(MainActivity.this, R.layout.wifi_layout, wifiInfo);
                                    listView.setAdapter(wa);
                                }
                            });
                        }else{
                            //when location service is not enabled
                            // open location settings
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                    }
                }
                //else
                    //networkType.setText("None");

            }
        });
        add.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Add data to dataset", Toast.LENGTH_SHORT).show());
    }

}