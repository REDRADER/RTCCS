package com.example.rtccs.AdminActivity.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.AdminLogin;
import com.example.rtccs.AdminActivity.ChangeDetails;
import com.example.rtccs.Model.Students;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentChangeDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;


public class ProfileFragment extends Fragment {

Button changedetails;

    ImageView img,mbac;
    TextView mfullname,mfullname2,memail,mphone,logout ;
    DatabaseReference dbref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_profile, container, false);
        logout=v.findViewById(R.id.logout);
        mfullname=v.findViewById(R.id.fullname);
        mfullname2=v.findViewById(R.id.fullname2);
        memail=v.findViewById(R.id.email);
        mphone=v.findViewById(R.id.phone);
        img=v.findViewById(R.id.profile_img);
        mbac=v.findViewById(R.id.bac);
        changedetails=v.findViewById(R.id.changedetail);

        changedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii=new Intent(getContext(), ChangeDetails.class);
                ii.putExtra("name",mfullname.getText());
                ii.putExtra("email",memail.getText());
                startActivity(ii);


            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent ii=new Intent(getContext(), AdminLogin.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ii);
            }
        });

        Paper.init(getContext());


        dbref= FirebaseDatabase.getInstance().getReference().child("Admins").child(Prevalent.currentonlineuser.getPhone());
        displayinfo();
        return v;
    }
    private void displayinfo() {

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Students studentsdata=snapshot.getValue(Students.class);
                    mfullname.setText(studentsdata.getFullname());
                    mfullname2.setText(studentsdata.getFullname());
                    memail.setText(studentsdata.getEmail());
                    mphone.setText(studentsdata.getPhone());
                    Picasso.get().load(studentsdata.getImage()).into(img);
                    Picasso.get().load(studentsdata.getImage()).into(mbac);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}