package com.example.rtccs.CommonActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rtccs.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullImage extends AppCompatActivity {
String img;
TextView title;
PhotoView fullimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        fullimg=findViewById(R.id.photo_view);
        title=findViewById(R.id.fulltitle);

        img=getIntent().getStringExtra("img");
        String stitle=getIntent().getStringExtra("title");


        title.setText(stitle);
        Glide.with(this).load(img).into(fullimg);

    }
}