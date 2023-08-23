package com.example.rtccs.AdminActivity.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.StudentActivity;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class StudentFragment extends Fragment {
    ListView listyear;
    ArrayList<String> arrayListlist=new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    private DatabaseReference dbref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_student, container, false);
        listyear=v.findViewById(R.id.list_year);
        arrayAdapter=new ArrayAdapter(getContext(), R.layout.array_simple_list_custom,arrayListlist);
        listyear.setAdapter(arrayAdapter);

      dbref=FirebaseDatabase.getInstance().getReference().child("Students");

      return v;



    }

    @Override
    public void onStart() {
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
                Intent ii=new Intent(getContext(), StudentActivity.class);
                ii.putExtra("year",selectedyears);
                startActivity(ii);

            }
        });
        super.onStart();
    }
}