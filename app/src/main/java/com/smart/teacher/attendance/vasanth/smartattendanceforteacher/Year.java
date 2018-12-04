package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Year extends AppCompatActivity {
    static String year="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
    }
    public void year1(View v){
        year = "1";
        startActivity(new Intent(Year.this, Branch.class));
    }
    public void year2(View v){
        year = "2";
        startActivity(new Intent(Year.this, Branch.class));
    }
    public void year3(View v){
        year = "3";
        startActivity(new Intent(Year.this, Branch.class));
    }
    public void year4(View v){
        year = "4";
        startActivity(new Intent(Year.this, Branch.class));
    }
}
