package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class Subjects extends AppCompatActivity {

    public static EditText newSubjectET;
    public static EditText pinForSubjectET;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceForPin;

    ArrayList<String> keysArrayList;
    ArrayList<String> namesForReference;
    ArrayList<CardClass> membersArrayList;
    static ArrayList<String> referenceForKeyArrayList;
    static ArrayList<String> referenceForNamesArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        TextView subjectNameTV = (TextView)findViewById(R.id.subjectNameTV);
        subjectNameTV.append(" "+Sections.section);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("addedSubjects").child(Year.year).child(Branch.branch).child(Sections.section);

        ListView listView = (ListView) findViewById(R.id.listViewForMember);



        membersArrayList = new ArrayList<>();
        keysArrayList = new ArrayList<>();
        namesForReference = new ArrayList<>();
        referenceForKeyArrayList = new ArrayList<>();
        referenceForNamesArrayList= new ArrayList<>();




        final AdapterClassForSubjects adapterForMember = new AdapterClassForSubjects(this, R.layout.card_design_for_subjects, membersArrayList);
        listView.setAdapter(adapterForMember);

        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String addedMember = dataSnapshot.getValue(String.class);
                membersArrayList.add(new CardClass(1, addedMember));
                namesForReference.add(addedMember);
                String addedkey = dataSnapshot.getKey();
                keysArrayList.add(addedkey);
                adapterForMember.notifyDataSetChanged();

                referenceForKeyArrayList = keysArrayList;
                referenceForNamesArrayList = namesForReference;
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
    public void addASubject(View v){
        newSubjectET = new EditText(this);
        pinForSubjectET = new EditText(this);

        newSubjectET.setHint("Enter the subject name");
        pinForSubjectET.setHint("Enter the secret PIN");

        AlertDialog.Builder builder = new AlertDialog.Builder(Subjects.this);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(newSubjectET);
        lay.addView(pinForSubjectET);
        builder.setView(lay,45,45,45,45);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newSubjectET.getText().toString().equals("") || pinForSubjectET.getText().toString().equals(""))
                    Toast.makeText(Subjects.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                else if(namesForReference.contains(newSubjectET.getText().toString()))
                    Toast.makeText(Subjects.this, "This name is already taken. Choose another name.", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(Subjects.this, "Subject is added", Toast.LENGTH_SHORT).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("addedSubjects").child(Year.year).child(Branch.branch).child(Sections.section);
                    databaseReference.push().setValue(newSubjectET.getText().toString());

                    databaseReferenceForPin = FirebaseDatabase.getInstance().getReference().child("pinForSubjects");
                    databaseReferenceForPin.child(Year.year+Branch.branch+Sections.section+newSubjectET.getText().toString()).setValue(pinForSubjectET.getText().toString());
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
        alert.setTitle("Enter the details");
        alert.show();
    }
}
