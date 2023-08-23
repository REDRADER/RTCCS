package com.example.rtccs.AdminActivity.Attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.Adapter.StudentsAdapter;
import com.example.rtccs.Adapter.StudentsAttendenceAdapter;
import com.example.rtccs.Model.Students;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Student_Attendence extends AppCompatActivity {
    RecyclerView mstudent_rec;
    TextView years_st,mclass,mdate;
    private DatabaseReference dbref;
    Button mfinish,mstd;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> studentpresent=new ArrayList<String>();
    private String year;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__attendence);
        mstudent_rec=findViewById(R.id.student_rec);
        mfinish=findViewById(R.id.end);
        mclass=findViewById(R.id.show_class);
        mdate=findViewById(R.id.show_date);
        mstd=findViewById(R.id.std_back);

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MM-yy");
        date=currentdate.format(calendar.getTime());
        year=getIntent().getStringExtra("year");


        mclass.setText(year);
        mdate.setText(date);

        mstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Student_Attendence.this, "arraylist "+studentpresent, Toast.LENGTH_SHORT).show();


                dbref= FirebaseDatabase.getInstance().getReference().child("Student Attendence").child(year).child(date);


                dbref.setValue(studentpresent).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Student_Attendence.this, date+" attendence is added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(Student_Attendence.this, " Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Student_Attendence.this, " Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mstudent_rec.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        mstudent_rec.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Students").child(year);
        FirebaseRecyclerOptions<Students> options=new FirebaseRecyclerOptions.Builder<Students>().setQuery(dbref,Students.class).build();
        FirebaseRecyclerAdapter<Students, StudentsAttendenceAdapter> adapter= new FirebaseRecyclerAdapter<Students, StudentsAttendenceAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StudentsAttendenceAdapter studentsAttendenceAdapter, int i, @NonNull Students students) {
                studentsAttendenceAdapter.name.setText(students.getFullname());
                studentsAttendenceAdapter.mpresent_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(studentpresent.add(students.getFullname())==true)
                        {
                            studentsAttendenceAdapter.name.setTextColor(Color.parseColor("#06d6a0"));
                            studentsAttendenceAdapter.mpresent_button.setEnabled(false);
                            studentsAttendenceAdapter.mabsent_button.setEnabled(true);
                        }
                        else
                        {

                        }

                    }
                });

                studentsAttendenceAdapter.mabsent_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(studentpresent.remove(new String(students.getFullname()))==true)
                        {
                            studentsAttendenceAdapter.name.setTextColor(Color.parseColor("#E63946"));
                            studentsAttendenceAdapter.mabsent_button.setEnabled(false);
                            studentsAttendenceAdapter.mpresent_button.setEnabled(true);

                        }
                        ;

                    }
                });
            }

            @NonNull
            @Override
            public StudentsAttendenceAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.students_attendence_item_layout,parent,false);
                StudentsAttendenceAdapter holder=new StudentsAttendenceAdapter(view);
                return holder;
            }
        };
        mstudent_rec.setAdapter(adapter);
        adapter.startListening();



    }
}