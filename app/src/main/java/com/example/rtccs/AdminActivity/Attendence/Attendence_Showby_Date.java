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
import android.widget.Toast;

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

public class Attendence_Showby_Date extends AppCompatActivity {
    ListView listdate;
    ArrayList<String> arrayListlist=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String type,year;
    Button mback;
    private DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence__showby__date);
        listdate=findViewById(R.id.list_date);
        arrayAdapter=new ArrayAdapter(getApplicationContext(), R.layout.array_simple_list_custom,arrayListlist);
        listdate.setAdapter(arrayAdapter);
        mback=findViewById(R.id.atten1_back);
        type=getIntent().getStringExtra("type");
        year=getIntent().getStringExtra("year");
        dbref= FirebaseDatabase.getInstance().getReference().child("Student Attendence").child(year);
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set=new HashSet<String>();
                Iterator i=snapshot.getChildren().iterator();
                while (i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selecteddate=((TextView)view).getText().toString();

                    Intent ii=new Intent(getApplicationContext(), Show_Attendence.class);
                    ii.putExtra("date",selecteddate);
                    ii.putExtra("year",year);


                    startActivity(ii);




            }
        });
        super.onStart();


          }
}