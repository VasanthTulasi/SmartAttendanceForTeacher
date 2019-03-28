package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.service.autofill.Dataset;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AttendanceInTeacher extends AppCompatActivity {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String formattedMonth = "";
    String key="";
    boolean codeCreatedByTeacher = false;
    Button manualAttendanceBtn;
    static int noofclasses =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_in_teacher);

        manualAttendanceBtn = (Button)findViewById(R.id.manualAttendance);


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    public void manualAttendance(View v){
            startActivity(new Intent(AttendanceInTeacher.this,ManualAttendance.class));
    }

    public void createCode(View v){
        codeCreatedByTeacher = true;
        EditText authCodeTV = (EditText) findViewById(R.id.authCode);
        EditText noOfClassesTV = (EditText)findViewById(R.id.noofclasses);
        noofclasses = Integer.parseInt(noOfClassesTV.getText().toString());



        final DatabaseReference dbForNoOfClasses = FirebaseDatabase.getInstance().getReference().child("noOfClasses");
        dbForNoOfClasses.child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName).setValue(noofclasses);

        final DatabaseReference dbForMakingAttendanceFalse = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);
        dbForMakingAttendanceFalse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(codeCreatedByTeacher) {
                        DatabaseReference dbForEachStudent = dbForMakingAttendanceFalse.child(ds.getKey());
                        dbForEachStudent.child("isAttendanceRecorded").setValue("false");
                    }
                }
                codeCreatedByTeacher = false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference dbForCode = FirebaseDatabase.getInstance().getReference().child("AuthCodes");
        key = Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName;
        if(!authCodeTV.getText().toString().equals("")) {
            Toast.makeText(AttendanceInTeacher.this, "Code created", Toast.LENGTH_SHORT).show();
            dbForCode.child(key).setValue(authCodeTV.getText().toString());
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            formattedMonth = df.format(c);
            addToNoOfClasses();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dbForCode.child(key).setValue("");
                }
            }, 1000*60);



            manualAttendanceBtn.setEnabled(false);
            manualAttendanceBtn.setBackgroundColor(Color.parseColor("#BF360C"));
            manualAttendanceBtn.setTextColor(Color.WHITE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(60* 1000);//min secs millisecs
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    AttendanceInTeacher.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            manualAttendanceBtn.setEnabled(true);
                            manualAttendanceBtn.setBackgroundColor(Color.parseColor("#F7DC6F"));
                            manualAttendanceBtn.setTextColor(Color.BLACK);
                        }
                    });
                }
            }).start();


        }else{
            Toast.makeText(AttendanceInTeacher.this, "Code cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToNoOfClasses(){


            final DatabaseReference databaseReferenceForAddingClasses = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child("classesConducted");
            databaseReferenceForAddingClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(formattedMonth)) {
                        getThePreviousValue();
                    } else {
                        databaseReferenceForAddingClasses.child(formattedMonth).setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    public void getThePreviousValue(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);

        final DatabaseReference preValRef = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child("classesConducted").child(formattedDate);

        preValRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long previousValue = dataSnapshot.getValue(Long.class);
                long presentVal = previousValue + 1;
                preValRef.setValue(presentVal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
