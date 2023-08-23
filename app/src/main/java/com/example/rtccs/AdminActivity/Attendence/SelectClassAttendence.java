package com.example.rtccs.AdminActivity.Attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rtccs.AdminActivity.StudentActivity;
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

public class SelectClassAttendence extends AppCompatActivity {
    ListView listyear;
    ArrayList<String> arrayListlist=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String type;
    Button mback;
    private DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class_attendence);
        listyear=findViewById(R.id.list_year);
        arrayAdapter=new ArrayAdapter(getApplicationContext(), R.layout.array_simple_list_custom,arrayListlist);
        listyear.setAdapter(arrayAdapter);
        type=getIntent().getStringExtra("type");
        mback=findViewById(R.id.atten_back);

        dbref= FirebaseDatabase.getInstance().getReference().child("Students");
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
        listyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedyears=((TextView)view).getText().toString();
                if (type.equals("take"))
                {
                    Intent ii=new Intent(getApplicationContext(), Student_Attendence.class);
                    ii.putExtra("year",selectedyears);
                    startActivity(ii);
                }
                else
                {
                    Intent ii=new Intent(getApplicationContext(), Attendence_Showby_Date.class);
                    ii.putExtra("year",selectedyears);
                    startActivity(ii);
                }


            }
        });
        super.onStart();
    }
}