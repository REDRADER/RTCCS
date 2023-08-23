package com.example.rtccs.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtccs.R;

public class GalleryAdapter extends RecyclerView.ViewHolder {

    public TextView date,caption,time;
    public ImageView img;
    public Button delete_photo;

    public GalleryAdapter(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.gallery_date);
        time=itemView.findViewById(R.id.gallery_time);
        caption=itemView.findViewById(R.id.gallery_caption);
        date=itemView.findViewById(R.id.gallery_date);
        img=itemView.findViewById(R.id.gallery_img);
        delete_photo=itemView.findViewById(R.id.delete_photo);
    }


}
