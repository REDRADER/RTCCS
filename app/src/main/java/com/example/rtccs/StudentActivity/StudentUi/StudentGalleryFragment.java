package com.example.rtccs.StudentActivity.StudentUi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rtccs.Adapter.GalleryAdapter;
import com.example.rtccs.CommonActivity.FullImage;
import com.example.rtccs.Model.Gallery;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class StudentGalleryFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_student_gallery, container, false);

        recyclerView=v.findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Gallery");

        return v;
    }

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<Gallery> options=new FirebaseRecyclerOptions.Builder<Gallery>().setQuery(dbref,Gallery.class).build();
        FirebaseRecyclerAdapter<Gallery, GalleryAdapter> adapter=new FirebaseRecyclerAdapter<Gallery, GalleryAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GalleryAdapter galleryAdapter, int i, @NonNull Gallery gallery) {

                galleryAdapter.caption.setText(gallery.getCaption());
                galleryAdapter.date.setText(gallery.getData());
                galleryAdapter.time.setText(gallery.getTime());
                Picasso.get().load(gallery.getImage()).into(galleryAdapter.img);
                galleryAdapter.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii=new Intent(getContext(), FullImage.class);
                        ii.putExtra("img",gallery.getImage());
                        ii.putExtra("title",gallery.getCaption());
                        startActivity(ii);
                    }
                });

            }

            @NonNull
            @Override
            public GalleryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.student_gallery_item_layout,parent,false);
                GalleryAdapter holder=new GalleryAdapter(v);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}