package com.example.rtccs.AdminActivity.Attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rtccs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Show_Attendence extends AppCompatActivity {
    ListView listatten;
    ArrayList<String> arrayListlist=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String date,year;
    private DatabaseReference dbref;
    Button mback;
    TextView mdate,myear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__attendence);
        listatten=findViewById(R.id.list_atten);
        mback=findViewById(R.id.atten3_back);

        mdate=findViewById(R.id.show_date);
        myear=findViewById(R.id.show_class);
        arrayAdapter=new ArrayAdapter(getApplicationContext(), R.layout.array_simple_list_custom_green,arrayListlist);
        listatten.setAdapter(arrayAdapter);
        date=getIntent().getStringExtra("date");
        year=getIntent().getStringExtra("year");

        mdate.setText(date);
        myear.setText(year);


        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbref= FirebaseDatabase.getInstance().getReference().child("Student Attendence").child(year).child(date);

    }

    @Override
    protected void onStart() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    arrayListlist.clear();
                    for (DataSnapshot ds:snapshot.getChildren())
                    {
                        String presstud=ds.getValue(String.class);
                        arrayListlist.add(presstud);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}