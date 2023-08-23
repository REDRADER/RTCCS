package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.Adapter.StudentsAdapter;
import com.example.rtccs.Model.Students;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class StudentActivity extends AppCompatActivity {
RecyclerView students;
TextView years_st;
private DatabaseReference dbref;
Button back;
RecyclerView.LayoutManager layoutManager;
    private String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        students=findViewById(R.id.students);
        years_st=findViewById(R.id.year_student);
        year=getIntent().getStringExtra("year");
        years_st.setText(year);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        students.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        students.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Students").child(year);

        FirebaseRecyclerOptions<Students> options=new FirebaseRecyclerOptions.Builder<Students>().setQuery(dbref,Students.class).build();
        FirebaseRecyclerAdapter<Students, StudentsAdapter> adapter=new FirebaseRecyclerAdapter<Students, StudentsAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StudentsAdapter studentsAdapter, int i, @NonNull Students students) {

                studentsAdapter.phone.setText(students.getPhone());
                studentsAdapter.name.setText(students.getFullname());
                studentsAdapter.email.setText(students.getEmail());
                studentsAdapter.year.setText(students.getYear());
                studentsAdapter.status.setText(students.getStatus());
               
                //DatabaseReference fref=FirebaseDatabase.getInstance().getReference().child("Students").child(students.getYear());
                studentsAdapter.mverify_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dbref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String,Object> up=new HashMap<>();
                                up.put("status","verified");
                                dbref.child(students.getPhone()).updateChildren(up).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        studentsAdapter.status.setText(students.getStatus());
                                        studentsAdapter.status.setTextColor(Color.parseColor("#06d6a0"));
                                        Toast.makeText(StudentActivity.this, students.getFullname()+" is verified ", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StudentActivity.this, "Something went wrong  ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                studentsAdapter.mnot_verify_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                HashMap<String,Object> up=new HashMap<>();
//                                up.put("status","notverified");
//                               fref.child(students.getPhone()).updateChildren(up).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        studentsAdapter.status.setText(students.getStatus());
//                                        Toast.makeText(StudentActivity.this, students.getFullname()+" is Not verified ", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(StudentActivity.this, "Something went wrong  ", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                        dbref.child(students.getPhone()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(StudentActivity.this, "Profile blocked", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(StudentActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
                if (students.getImage()!=null)
                {
                    Picasso.get().load(students.getImage()).into(studentsAdapter.img);
                }
                else
                {

                }


            }

            @NonNull
            @Override
            public StudentsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.students_item_layout,parent,false);
                StudentsAdapter holder=new StudentsAdapter(view);
                return holder;
            }
        };
        students.setAdapter(adapter);
        adapter.startListening();

    }

}