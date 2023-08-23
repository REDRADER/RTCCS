package com.example.rtccs.StudentActivity.StudentUi;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rtccs.AdminActivity.ui.ChatFragment;
import com.example.rtccs.AdminActivity.ui.FacultyFragment;
import com.example.rtccs.AdminActivity.ui.StudentFragment;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentIndexActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class StudentHomeFragment extends Fragment {
    ImageSlider imageSlider;
    Button mbt1,mbt2,mbt3,mbt4;
    DatabaseReference dbref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=  inflater.inflate(R.layout.fragment_student_home, container, false);
        dbref= FirebaseDatabase.getInstance().getReference().child("Students").child(Prevalent.currentonlineuser.getYear()).child(Prevalent.currentonlineuser.getPhone());

        imageSlider = v.findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img_1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img_2,ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img_3,ScaleTypes.FIT));

        mbt1=v.findViewById(R.id.btn1);
        mbt2=v.findViewById(R.id.btn2);
        mbt3=v.findViewById(R.id.btn3);
        mbt4=v.findViewById(R.id.btn4);


        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        mbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new StudentFacultyFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.Student_fragment, fragment).commit();
            }
        });
        mbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
            }
        });
        mbt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.rtccs.co.in/"));
                startActivity(viewIntent);
            }
        });



        return v;
    }

    private void chat()
    {
        dbref= FirebaseDatabase.getInstance().getReference().child("Chat").child(Prevalent.currentonlineuser.getYear());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri=snapshot.child("uri").getValue(String.class);
                // Toast.makeText(StudentIndexActivity.this, ""+uri, Toast.LENGTH_SHORT).show();
                if (uri!=null)
                {
                    Intent yyt =new Intent("android.intent.action.VIEW",
                            Uri.parse(uri));
                    startActivity(yyt);
                }
                else
                {
                    Toast.makeText(getContext(), "chat not loading chat ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error loading chat ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}