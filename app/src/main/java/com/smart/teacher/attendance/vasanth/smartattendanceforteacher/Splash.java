package com.smart.teacher.attendance.vasanth.smartattendanceforteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView gello =(TextView)findViewById(R.id.gello);
        gello.setText(AdapterClassForCardDetailsForMember.subjectName);
    }
}
