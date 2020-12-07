package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterClassForManualAttendance extends ArrayAdapter<CardClass> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static int positionValue;
    static String studentName = "";
    static long noClasses = 1L;
    boolean isBtnClicked = false;

    private static class ViewHolder {
        TextView numberForMember;
        TextView nameForMember;
    }

    public AdapterClassForManualAttendance(Context context, int resource, ArrayList<CardClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        //sets up the image loader library

        //get the persons information
        final int numberForMember= getItem(position).getIndexNumber();

        String nameForMember = getItem(position).getNameOfPerson();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;



        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.numberForMember= (TextView) convertView.findViewById(R.id.numberForMember);
            holder.nameForMember = (TextView) convertView.findViewById(R.id.nameForMember);
            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        final CardView regStuCard = (CardView) convertView.findViewById(R.id.registeredStudentCard);


        DatabaseReference dbFornoOfClasses = FirebaseDatabase.getInstance().getReference().child("noOfClasses");
        dbFornoOfClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noClasses = dataSnapshot.child(Year.year + Branch.branch + Sections.section + AdapterClassForSubjects.subjectName).getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        regStuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnClicked = true;

                studentName = ManualAttendance.referenceForNamesArrayListInStudentsManual.get(position);

                        DatabaseReference dbForManAttendance = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child(studentName);
                        dbForManAttendance.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(isBtnClicked) {
                                    DatabaseReference dbForNoOfClasses = FirebaseDatabase.getInstance().getReference().child("noOfClasses");
                                    dbForNoOfClasses.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            noClasses = dataSnapshot.child(Year.year + Branch.branch + Sections.section + AdapterClassForSubjects.subjectName).getValue(Long.class);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    if (dataSnapshot.child("isAttendanceRecorded").getValue(String.class).equals("false"))
                                        enterDataOnToCloud();
                                    else
                                        Toast.makeText(mContext, "Attendance for this student is already recorded", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        });


        positionValue = position;
        holder.numberForMember.setText(String.valueOf(position+1));
        holder.nameForMember.setText(nameForMember);


        return convertView;

    }

    public void enterDataOnToCloud(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c);

        final DatabaseReference databaseReferenceForDateEntry = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child(studentName);
        databaseReferenceForDateEntry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(isBtnClicked) {
                    if (snapshot.hasChild(formattedDate)) {
                        getThePreviousValue();
                    } else {
                        Toast.makeText(mContext, "Attendance is recorded for " + studentName, Toast.LENGTH_LONG).show();
                        databaseReferenceForDateEntry.child(formattedDate).setValue(noClasses);
                        DatabaseReference dbForAttendaceRecord = databaseReferenceForDateEntry.child("isAttendanceRecorded");
                        dbForAttendaceRecord.setValue("true");
                    }
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

        final DatabaseReference preValRef = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child(studentName).child(formattedDate);

        preValRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isBtnClicked) {
                    Toast.makeText(mContext, "Attendance is recorded for " + studentName, Toast.LENGTH_LONG).show();
                    long previousValue = dataSnapshot.getValue(Long.class);
                    long presentVal = previousValue + noClasses;
                    preValRef.setValue(presentVal);
                    DatabaseReference dbForAttendaceRecord = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child(studentName).child("isAttendanceRecorded");
                    dbForAttendaceRecord.setValue("true");
                    isBtnClicked = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
