package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterClassForSubjects extends ArrayAdapter<CardClass> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static int positionValue;
    static String subjectName = "";

    private static class ViewHolder {
        TextView numberForMember;
        TextView nameForMember;
    }

    public AdapterClassForSubjects(Context context, int resource, ArrayList<CardClass> objects) {
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

        final CardView subjectCard = (CardView) convertView.findViewById(R.id.subjectCard);



        subjectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 subjectName = Subjects.referenceForNamesArrayList.get(position);
                 validatePin();
            }
        });







        positionValue = position;
        holder.numberForMember.setText(String.valueOf(position+1));
        holder.nameForMember.setText(nameForMember);



        return convertView;

    }

    @SuppressLint("RestrictedApi")
    public void validatePin(){
        final EditText pinForSubjectET = new EditText(mContext);
        pinForSubjectET.setHint("Enter the secret PIN");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(pinForSubjectET,45,45,45,45);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference databaseReferenceForPin = FirebaseDatabase.getInstance().getReference().child("pinForSubjects");
                databaseReferenceForPin.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s ="";
                        s = dataSnapshot.child(Year.year+Branch.branch+Sections.section+AdapterClassForSubjects.subjectName).getValue(String.class);
                        if (pinForSubjectET.getText().toString().equals(s))
                            mContext.startActivity(new Intent(mContext, Students.class));
                        else
                            Toast.makeText(mContext, "Incorrect pin!\nContact the admin if you forgot the PIN.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Enter the PIN");
        alert.show();
    }

}
