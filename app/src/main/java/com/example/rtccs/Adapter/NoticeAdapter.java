package com.example.rtccs.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtccs.R;


public class NoticeAdapter  extends RecyclerView.ViewHolder  {

    public TextView date,title,time;
    public ImageView img;
    public Button delete_notice;

    public NoticeAdapter(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.notice_date);
        time=itemView.findViewById(R.id.notice_time);
        title=itemView.findViewById(R.id.notice_title);
        date=itemView.findViewById(R.id.notice_date);
        img=itemView.findViewById(R.id.notice_img);
        delete_notice=itemView.findViewById(R.id.delete_notice);




    }
}
