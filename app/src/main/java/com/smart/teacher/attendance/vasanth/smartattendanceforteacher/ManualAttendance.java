package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManualAttendance extends AppCompatActivity {

    DatabaseReference databaseReferenceInStudents;

    ArrayList<String> keysArrayListInStudents;
    ArrayList<String> namesForReferenceInStudents;
    ArrayList<CardClass> membersArrayListInStudents;
    static ArrayList<String> referenceForKeyArrayListInStudentsManual;
    static ArrayList<String> referenceForNamesArrayListInStudentsManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_attendance);

        databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);

        ListView listViewInStudents = (ListView) findViewById(R.id.listViewOfAvailableStudents);

        membersArrayListInStudents = new ArrayList<>();
        keysArrayListInStudents = new ArrayList<>();
        namesForReferenceInStudents = new ArrayList<>();
        referenceForKeyArrayListInStudentsManual = new ArrayList<>();
        referenceForNamesArrayListInStudentsManual= new ArrayList<>();

        final AdapterClassForManualAttendance adapterForManualAttendance = new AdapterClassForManualAttendance(this, R.layout.card_design_for_manual_attendance, membersArrayListInStudents);
        listViewInStudents.setAdapter(adapterForManualAttendance);


    /*    ValueEventListener eventListerner = */ databaseReferenceInStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()){
                    DatabaseReference databaseReferenceForChild = databaseReferenceInStudents.child(ds.getKey());
                    databaseReferenceForChild.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(String.valueOf(dataSnapshot.child("isAttendanceRecorded").getValue()).equals("false")){
                                if(!namesForReferenceInStudents.contains(String.valueOf(ds.getKey()))) {

                                    String addedMember = String.valueOf(ds.getKey());
                                    membersArrayListInStudents.add(new CardClass(1, addedMember));
                                    namesForReferenceInStudents.add(addedMember);
                                    String addedkey = dataSnapshot.getKey();
                                    keysArrayListInStudents.add(addedkey);
                                    adapterForManualAttendance.notifyDataSetChanged();

                                    referenceForKeyArrayListInStudentsManual = keysArrayListInStudents;
                                    referenceForNamesArrayListInStudentsManual = namesForReferenceInStudents;
                                }
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

//        databaseReferenceInStudents.removeEventListener(eventListerner);

    }
}
