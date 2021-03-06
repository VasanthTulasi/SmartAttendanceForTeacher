package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterClassForStudents extends ArrayAdapter<CardClass> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public static int positionValue;
    static String subjectName = "",studentName="";


    private static class ViewHolder {
        TextView numberForMember;
        TextView nameForMember;
        TextView rollNumberForMember;
    }

    public AdapterClassForStudents(Context context, int resource, ArrayList<CardClass> objects) {
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
        String rollNumberForMember = getItem(position).getRollNumberForMember();

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
            holder.rollNumberForMember = (TextView)convertView.findViewById(R.id.rollNumberForMember);
            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        final CardView studentCard = (CardView) convertView.findViewById(R.id.studentCard);

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentName = Students.namesForReferenceInStudents.get(position);
                mContext.startActivity(new Intent(mContext,CheckAttendanceOfStudents.class));
            }
        });






        positionValue = position;
        holder.numberForMember.setText(String.valueOf(position+1));
        holder.nameForMember.setText(nameForMember);
        holder.rollNumberForMember.setText(rollNumberForMember);



        return convertView;

    }

}
