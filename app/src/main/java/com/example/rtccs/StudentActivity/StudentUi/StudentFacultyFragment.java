package com.example.rtccs.StudentActivity.StudentUi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rtccs.Adapter.TeacherAdapter;
import com.example.rtccs.AdminActivity.UpdateFaculty;
import com.example.rtccs.Model.Teachers;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class StudentFacultyFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference dbref;
    private TeacherAdapter teacherAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.fragment_student_faculty, container, false);
        recyclerView=v.findViewById(R.id.teachers);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Teachers");
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
                if (!teachers.getImage().equals("")) {
                    Picasso.get().load(teachers.getImage()).into(teacherAdapter.imageView);
                }
            }

            @NonNull
            @Override
            public TeacherAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.student_teachers_item_layout,parent,false);
                TeacherAdapter holder=new TeacherAdapter(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}