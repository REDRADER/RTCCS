package com.example.rtccs.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtccs.R;

public class StudentsAttendenceAdapter extends RecyclerView.ViewHolder {
    public TextView name;

    public Button mpresent_button,mabsent_button;
    public StudentsAttendenceAdapter(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);

        mpresent_button=itemView.findViewById(R.id.present_button);
        mabsent_button=itemView.findViewById(R.id.absent_button);



    }
}
