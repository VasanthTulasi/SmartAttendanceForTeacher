package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Students extends AppCompatActivity {
    public static EditText newStudentET;
    DatabaseReference databaseReferenceInStudents;

    ArrayList<String> keysArrayListInStudents;
    ArrayList<String> namesForReferenceInStudents;
    ArrayList<CardClass> membersArrayListInStudents;
    static ArrayList<String> referenceForKeyArrayListInStudents;
    static ArrayList<String> referenceForNamesArrayListInStudents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        TextView studentsTV = (TextView)findViewById(R.id.studentsTV);
        studentsTV.append(" "+AdapterClassForCardDetailsForMember.subjectName);

        databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForCardDetailsForMember.subjectName);
        ListView listViewInStudents = (ListView) findViewById(R.id.listViewForMemberInStudentActivity);

        membersArrayListInStudents = new ArrayList<>();
        keysArrayListInStudents = new ArrayList<>();
        namesForReferenceInStudents = new ArrayList<>();
        referenceForKeyArrayListInStudents = new ArrayList<>();
        referenceForNamesArrayListInStudents= new ArrayList<>();

        final AdapterClassForCardDetailsForMemberInStudent adapterForMemberInStudents = new AdapterClassForCardDetailsForMemberInStudent(this, R.layout.card_design_for_member_in_student, membersArrayListInStudents);
        listViewInStudents.setAdapter(adapterForMemberInStudents);

        databaseReferenceInStudents.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String addedMember = dataSnapshot.getValue(String.class);
                membersArrayListInStudents.add(new CardClass(1, addedMember));
                namesForReferenceInStudents.add(addedMember);
                String addedkey = dataSnapshot.getKey();
                keysArrayListInStudents.add(addedkey);
                adapterForMemberInStudents.notifyDataSetChanged();

                //Changed comment

                referenceForKeyArrayListInStudents = keysArrayListInStudents;
                referenceForNamesArrayListInStudents = namesForReferenceInStudents;
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
        newStudentET = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(Students.this);
        builder.setView(newStudentET, 45, 45, 45, 45);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newStudentET.getText().toString().equals(""))
                    Toast.makeText(Students.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                else if(namesForReferenceInStudents.contains(newStudentET.getText().toString()))
                    Toast.makeText(Students.this, "This name is already taken. Choose another name.", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(Students.this, "Student is added", Toast.LENGTH_SHORT).show();
                    databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForCardDetailsForMember.subjectName);
                    databaseReferenceInStudents.push().setValue(newStudentET.getText().toString());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Enter the name of the student");
        alert.show();
    }
}
