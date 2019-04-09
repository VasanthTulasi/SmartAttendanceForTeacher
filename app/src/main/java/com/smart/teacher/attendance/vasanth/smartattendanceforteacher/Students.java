package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Students extends AppCompatActivity {
    public static EditText newStudentET;
    DatabaseReference databaseReferenceInStudents;

    ArrayList<String> keysArrayListInStudents;
    static ArrayList<String> namesForReferenceInStudents;
    ArrayList<CardClass> membersArrayListInStudents;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        TextView studentsTV = (TextView)findViewById(R.id.studentsTV);
        studentsTV.append(" "+ AdapterClassForSubjects.subjectName);

        databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);
        ListView listViewInStudents = (ListView) findViewById(R.id.listViewForMemberInStudentActivity);

        membersArrayListInStudents = new ArrayList<>();
        keysArrayListInStudents = new ArrayList<>();
        namesForReferenceInStudents = new ArrayList<>();


        final AdapterClassForStudents adapterForMemberInStudents = new AdapterClassForStudents(this, R.layout.card_design_for_students, membersArrayListInStudents);
        listViewInStudents.setAdapter(adapterForMemberInStudents);

            databaseReferenceInStudents.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String addedMember = dataSnapshot.getValue(String.class);

                final DatabaseReference dbToFindRollNumber = FirebaseDatabase.getInstance().getReference().child("emails");
                dbToFindRollNumber.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            DatabaseReference dbToFindRollNumberInsideEachKey = dbToFindRollNumber.child(ds.getKey());
                            dbToFindRollNumberInsideEachKey.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Name").getValue().equals(addedMember)) {
                                        String rollNumber = String.valueOf(dataSnapshot.child("RollNumber").getValue());
                                        membersArrayListInStudents.add(new CardClass(1, addedMember, rollNumber));
                                        namesForReferenceInStudents.add(addedMember);
                                        String addedkey = dataSnapshot.getKey();
                                        keysArrayListInStudents.add(addedkey);
                                        adapterForMemberInStudents.notifyDataSetChanged();

//                                        referenceForKeyArrayListInStudents = keysArrayListInStudents;
//                                        referenceForNamesArrayListInStudents = namesForReferenceInStudents;
//
//                                        if (pb != null) {
//                                            pb.setVisibility(View.GONE);
//                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                String addedMember = dataSnapshot.getValue(String.class);
//                membersArrayListInStudents.add(new CardClass(1, addedMember));
//                namesForReferenceInStudents.add(addedMember);
//                String addedkey = dataSnapshot.getKey();
//                keysArrayListInStudents.add(addedkey);
//                adapterForMemberInStudents.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s){

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("RestrictedApi")
    public void addAStudent(View v){
        startActivity(new Intent(Students.this,RegisteredStudents.class));
        //      newStudentET = new EditText(this);
//
       //          AlertDialog.Builder builder = new AlertDialog.Builder(Students.this);
//        builder.setView(newStudentET, 45, 45, 45, 45);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (newStudentET.getText().toString().equals(""))
//                    Toast.makeText(Students.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
//                else if(namesForReferenceInStudents.contains(newStudentET.getText().toString()))
//                    Toast.makeText(Students.this, "This name is already taken. Choose another name.", Toast.LENGTH_LONG).show();
//                else {
//                    Toast.makeText(Students.this, "Student is added", Toast.LENGTH_SHORT).show();
//                    databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);
//                    databaseReferenceInStudents.push().setValue(newStudentET.getText().toString());
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.setTitle("Enter the name of the student");
//        alert.show();
    }


    public void recordAttendance(View v){
        startActivity(new Intent(Students.this,AttendanceInTeacher.class));
    }

    public void about(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(Students.this);

        builder.setMessage("Click on any one of the student names to know his/her attendance.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Instruction!");
        alert.show();
    }

}
