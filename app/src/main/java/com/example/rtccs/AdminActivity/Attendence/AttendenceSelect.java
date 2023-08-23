package com.example.rtccs.AdminActivity.Attendence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rtccs.R;

public class AttendenceSelect extends AppCompatActivity {
Button mtakeatten,mshowatten,matten_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_select);
        mtakeatten=findViewById(R.id.take_atten);
        mshowatten=findViewById(R.id.show_atten);
        matten_back=findViewById(R.id.atten0_back);

        matten_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtakeatten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iu=new Intent(getApplicationContext(),SelectClassAttendence.class);
                iu.putExtra("type","take");
                startActivity(iu);
            }
        });

        mshowatten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iu=new Intent(getApplicationContext(),SelectClassAttendence.class);
                iu.putExtra("type","show");
                startActivity(iu);
            }
        });
    }
}