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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterClassForRegisteredStudents extends ArrayAdapter<CardClass> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static int positionValue;
    static String studentName = "";

    private static class ViewHolder {
        TextView numberForMember;
        TextView nameForMember;
    }

    public AdapterClassForRegisteredStudents(Context context, int resource, ArrayList<CardClass> objects) {
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


        regStuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ArrayList<String> al = new ArrayList<>();
                studentName = RegisteredStudents.referenceForNamesArrayListInStudents.get(position);
                final DatabaseReference dbRefToAddStudent = FirebaseDatabase.getInstance().getReference().child("addedStudents").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName);

                if(!Students.namesForReferenceInStudents.contains(studentName)) {
                    dbRefToAddStudent.push().setValue(studentName);
                    mContext.startActivity(new Intent(mContext,Students.class));
                    Toast.makeText(mContext,studentName+" is added in the list of students",Toast.LENGTH_LONG).show();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("attendance").child(Year.year).child(Branch.branch).child(Sections.section).child(AdapterClassForSubjects.subjectName).child(studentName);
                    db.child("isAttendanceRecorded").setValue("false");

                    db = FirebaseDatabase.getInstance().getReference().child("noOfClasses");
                    db.child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName).setValue(1);

                }
                else
                    Toast.makeText(mContext,"Student already exists",Toast.LENGTH_SHORT).show();

            }
        });


        positionValue = position;
        holder.numberForMember.setText(String.valueOf(position+1));
        holder.nameForMember.setText(nameForMember);



        return convertView;

    }

}
