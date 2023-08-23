package com.example.rtccs.AdminActivity.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rtccs.Adapter.GalleryAdapter;
import com.example.rtccs.AdminActivity.AddNotice;
import com.example.rtccs.AdminActivity.AddtoGallery;
import com.example.rtccs.CommonActivity.FullImage;
import com.example.rtccs.Model.Gallery;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class GalleryFragment extends Fragment {
    FloatingActionButton efa;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbref;
    AlertDialog.Builder builder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView=v.findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        builder = new AlertDialog.Builder(getContext());
        efa=v.findViewById(R.id.add_image_gallery);
        efa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddtoGallery.class));
            }
        });

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
                galleryAdapter.delete_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setMessage("Do you really want to delete the photo!").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbref.child(gallery.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Photo deleted succefully", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert=builder.create();
                        alert.setTitle("Delete Photo");
                        alert.show();

                    }
                });
            }



            @NonNull
            @Override
            public GalleryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_layout,parent,false);
               GalleryAdapter holder=new GalleryAdapter(v);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}