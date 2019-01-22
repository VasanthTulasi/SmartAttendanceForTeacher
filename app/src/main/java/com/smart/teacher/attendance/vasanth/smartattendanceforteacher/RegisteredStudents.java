package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RegisteredStudents extends AppCompatActivity {

    DatabaseReference databaseReferenceInStudents;

    ArrayList<String> keysArrayListInStudents;
    ArrayList<String> namesForReferenceInStudents;
    ArrayList<CardClass> membersArrayListInStudents;
    static ArrayList<String> referenceForKeyArrayListInStudents;
    static ArrayList<String> referenceForNamesArrayListInStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_students);


        databaseReferenceInStudents = FirebaseDatabase.getInstance().getReference().child("emails");

        ListView listViewInStudents = (ListView) findViewById(R.id.listViewOfAvailableStudents);

        membersArrayListInStudents = new ArrayList<>();
        keysArrayListInStudents = new ArrayList<>();
        namesForReferenceInStudents = new ArrayList<>();
        referenceForKeyArrayListInStudents = new ArrayList<>();
        referenceForNamesArrayListInStudents= new ArrayList<>();

        final AdapterClassForRegisteredStudents adapterForRegisteredStudents = new AdapterClassForRegisteredStudents(this, R.layout.card_design_for_registered_students, membersArrayListInStudents);
        listViewInStudents.setAdapter(adapterForRegisteredStudents);


        databaseReferenceInStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    DatabaseReference databaseReferenceForChild = databaseReferenceInStudents.child(ds.getKey());
                    databaseReferenceForChild.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(String.valueOf(dataSnapshot.child("Branch").getValue()).equals(Branch.branch) && String.valueOf(dataSnapshot.child("Year").getValue()).equals(Year.year) && String.valueOf(dataSnapshot.child("Section").getValue()).equals(Sections.section)){

                                if(!namesForReferenceInStudents.contains(String.valueOf(dataSnapshot.child("Name").getValue())))
                                {
                                    String addedMember = String.valueOf(dataSnapshot.child("Name").getValue());
                                    membersArrayListInStudents.add(new CardClass(1, addedMember));
                                    namesForReferenceInStudents.add(addedMember);
                                    String addedkey = dataSnapshot.getKey();
                                    keysArrayListInStudents.add(addedkey);
                                    adapterForRegisteredStudents.notifyDataSetChanged();

                                    referenceForKeyArrayListInStudents = keysArrayListInStudents;
                                    referenceForNamesArrayListInStudents = namesForReferenceInStudents;
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

        //        databaseReferenceInStudents.addChildEventListener(new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                String addedMember = dataSnapshot.getValue(String.class);
//                membersArrayListInStudents.add(new CardClass(1, addedMember));
//                namesForReferenceInStudents.add(addedMember);
//                String addedkey = dataSnapshot.getKey();
//                keysArrayListInStudents.add(addedkey);
//                adapterForRegisteredStudents.notifyDataSetChanged();
//
//                referenceForKeyArrayListInStudents = keysArrayListInStudents;
//                referenceForNamesArrayListInStudents = namesForReferenceInStudents;
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s){
//
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

}
