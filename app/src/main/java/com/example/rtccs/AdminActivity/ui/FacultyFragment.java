package com.example.rtccs.AdminActivity.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rtccs.Adapter.TeacherAdapter;
import com.example.rtccs.AdminActivity.AddFaculty;
import com.example.rtccs.AdminActivity.UpdateFaculty;
import com.example.rtccs.Model.Teachers;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FacultyFragment extends Fragment {

    FloatingActionButton add_fac;
    RecyclerView recyclerView;
   RecyclerView.LayoutManager layoutManager;

    private DatabaseReference dbref;
    private TeacherAdapter teacherAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_faculty, container, false);

        recyclerView=v.findViewById(R.id.teachers);
        recyclerView.setHasFixedSize(true);
        add_fac=v.findViewById(R.id.add_faculty);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbref=FirebaseDatabase.getInstance().getReference().child("Teachers");

//        reference= FirebaseDatabase.getInstance().getReference().child("Teachers");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            list=new ArrayList<>();
//            if (!dataSnapshot.exists())
//            {
//                Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    Teachers data=snapshot.getValue(Teachers.class);
//                    list.add(data);
//                    teacherAdapter=new TeacherAdapter(list,getContext());
//                    recyclerView.setAdapter(teacherAdapter);
//                }
//            }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "database error", Toast.LENGTH_SHORT).show();
//            }
//        });

        add_fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddFaculty.class));
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<Teachers> options=new FirebaseRecyclerOptions.Builder<Teachers>()
                .setQuery(dbref,Teachers.class).build();
        FirebaseRecyclerAdapter<Teachers,TeacherAdapter> adapter=new FirebaseRecyclerAdapter<Teachers, TeacherAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TeacherAdapter teacherAdapter, int i, @NonNull Teachers teachers) {
            teacherAdapter.name.setText(teachers.getName());
                teacherAdapter.email.setText(teachers.getEmail());
                teacherAdapter.phone.setText(teachers.getPhone());
                teacherAdapter.subject.setText(teachers.getSubject());

               if (!teachers.getImage().equals(""))
               {
                   Picasso.get().load(teachers.getImage()).into(teacherAdapter.imageView);
               }

    teacherAdapter.update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent ins=new Intent(getContext(), UpdateFaculty.class);
            ins.putExtra("name",teachers.getName());
            ins.putExtra("email",teachers.getEmail());
            ins.putExtra("phone",teachers.getPhone());
            ins.putExtra("subject",teachers.getSubject());
            ins.putExtra("img",teachers.getImage());
            ins.putExtra("uniquekey",teachers.getKey());

            startActivity(ins);
        }
    });
            }

            @NonNull
            @Override
            public TeacherAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_item_layout,parent,false);
            TeacherAdapter holder=new TeacherAdapter(view);
            return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}