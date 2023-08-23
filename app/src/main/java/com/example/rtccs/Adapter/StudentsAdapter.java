package com.example.rtccs.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtccs.R;

public class StudentsAdapter extends RecyclerView.ViewHolder {
    public TextView name,phone,email,year,status;
    public  ImageView img;
    public Button mverify_button,mnot_verify_button;
    public StudentsAdapter(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        email=itemView.findViewById(R.id.email);
        phone=itemView.findViewById(R.id.phone);
        year=itemView.findViewById(R.id.years);
        status=itemView.findViewById(R.id.status);
        img=itemView.findViewById(R.id.img_student);
        mverify_button=itemView.findViewById(R.id.verify_button);
        mnot_verify_button=itemView.findViewById(R.id.not_verify_button);



    }
}
