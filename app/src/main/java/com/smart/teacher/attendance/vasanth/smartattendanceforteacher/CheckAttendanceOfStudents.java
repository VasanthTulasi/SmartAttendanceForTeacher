package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckAttendanceOfStudents extends AppCompatActivity {
    DatabaseReference dbForCheckingAttendance,databaseReferenceForTotalClasses;
    long attendedClasses,totalClasses;
    Spinner studentSpi;
    ArrayList<String> arraySpinnerForStudent;
    TextView attendedClassesTV,workingClassesTV,dbTV3;
    static String studentName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance_of_students);
        arraySpinnerForStudent = new ArrayList<String>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                 arraySpinnerForStudent.add(String.valueOf(ds.getValue()));
                }
                int pos = arraySpinnerForStudent.indexOf(AdapterClassForStudents.studentName);

                studentSpi = findViewById(R.id.studentSpi);
                ArrayAdapter<String> adapterForYear = new ArrayAdapter<String>(CheckAttendanceOfStudents.this, R.layout.spinner_item, arraySpinnerForStudent);
                adapterForYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                studentSpi.setAdapter(adapterForYear);
                studentSpi.setSelection(pos);
                studentSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        studentName = String.valueOf(studentSpi.getItemAtPosition(i));
                        getAttendedClasses(studentName);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getAttendedClasses(final String studentName){
        attendedClassesTV = findViewById(R.id.attendedClassesTV);
        dbForCheckingAttendance = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);
        dbForCheckingAttendance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(studentName).exists()) {
                    DatabaseReference db =dbForCheckingAttendance.child(studentName);
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds1 : dataSnapshot.getChildren())
                                if(!ds1.getValue().equals("false") && !ds1.getValue().equals("true"))//ds1: Each date
                                    if (ds1.getValue(Long.class) != null)
                                        attendedClasses += ds1.getValue(Long.class);
                                    attendedClassesTV.setText("Number of attended classes: " + String.valueOf(attendedClasses));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    countTotalClasses();
                }else{
                    dbTV3 = findViewById(R.id.dbTV3);
                    attendedClasses = 0;
                    attendedClassesTV.setText("Number of attended classes: 0");
                    dbTV3.setText("0%");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void countTotalClasses(){
        workingClassesTV = findViewById(R.id. workingClassesTV);
        dbTV3 = findViewById(R.id.dbTV3);
        databaseReferenceForTotalClasses = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child("classesConducted");
        databaseReferenceForTotalClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren())
                    if(!ds1.getValue().equals("false") && !ds1.getValue().equals("true"))
                        if (ds1.getValue(Long.class) != null)
                            totalClasses += ds1.getValue(Long.class);

                    workingClassesTV.setText("Number of conducted classes: "+String.valueOf(totalClasses));
                    double finalAttendance = (double)(attendedClasses*100)/totalClasses;
                    dbTV3.setText(String.valueOf(new DecimalFormat("##.##").format(finalAttendance))+"%");
                    finalAttendance = 0;
                    totalClasses=0;
                    attendedClasses=0;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void overallAttendance(View v){
        startActivity(new Intent(CheckAttendanceOfStudents.this,OverallAttendance.class));
    }
}
