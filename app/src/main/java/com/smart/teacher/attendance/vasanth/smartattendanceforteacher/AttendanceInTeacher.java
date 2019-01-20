package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AttendanceInTeacher extends AppCompatActivity {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_in_teacher);


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    public void createCode(View v){
        TextView authCodeTV = (TextView) findViewById(R.id. authCode);
        DatabaseReference dbForCode = FirebaseDatabase.getInstance().getReference().child("AuthCodes");
        String key = Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName;
        dbForCode.child(key).setValue(authCodeTV.getText().toString());
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(AttendanceInTeacher.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (AttendanceInTeacher.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AttendanceInTeacher.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                DatabaseReference dbForLocationCoordinates = FirebaseDatabase.getInstance().getReference().child("LocationCoordinates").child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName);
                dbForLocationCoordinates.child("latitude").setValue(latti);
                dbForLocationCoordinates.child("longitude").setValue(longi);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();

                DatabaseReference dbForLocationCoordinates = FirebaseDatabase.getInstance().getReference().child("LocationCoordinates").child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName);
                dbForLocationCoordinates.child("latitude").setValue(latti);
                dbForLocationCoordinates.child("longitude").setValue(longi);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();


                DatabaseReference dbForLocationCoordinates = FirebaseDatabase.getInstance().getReference().child("LocationCoordinates").child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName);
                dbForLocationCoordinates.child("latitude").setValue(latti);
                dbForLocationCoordinates.child("longitude").setValue(longi);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
