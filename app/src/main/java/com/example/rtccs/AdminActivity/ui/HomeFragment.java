package com.example.rtccs.AdminActivity.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rtccs.AdminActivity.Attendence.AttendenceSelect;
import com.example.rtccs.AdminActivity.Attendence.Student_Attendence;
import com.example.rtccs.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageSlider imageSlider;
    Button mbt1,mbt2,mbt3,mbt4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_home, container, false);
         imageSlider = v.findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img_1,ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img_2,ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img_3,ScaleTypes.FIT));


        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        mbt1=v.findViewById(R.id.btn1);
        mbt2=v.findViewById(R.id.btn2);
        mbt3=v.findViewById(R.id.btn3);
        mbt4=v.findViewById(R.id.btn4);


        mbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new StudentFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment).commit();
            }
        });
        mbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new FacultyFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment).commit();
            }
        });
        mbt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ChatFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment).commit();
            }
        });
        mbt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent viewIntent =
//                        new Intent("android.intent.action.VIEW",
//                                Uri.parse("http://www.rtccs.co.in/"));
                Intent viewIntent=new Intent(getContext(), AttendenceSelect.class);
                startActivity(viewIntent);
            }
        });




       return v;
    }
}