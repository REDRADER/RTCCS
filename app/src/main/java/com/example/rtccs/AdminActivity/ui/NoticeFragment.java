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

import com.example.rtccs.Adapter.NoticeAdapter;
import com.example.rtccs.AdminActivity.AddNotice;
import com.example.rtccs.CommonActivity.FullImage;
import com.example.rtccs.Model.Notice;
import com.example.rtccs.Model.Teachers;
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

import java.util.List;

public class NoticeFragment extends Fragment {

FloatingActionButton efa;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AlertDialog.Builder builder;
    private DatabaseReference dbref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notice, container, false);

        recyclerView=v.findViewById(R.id.notice);
        recyclerView.setHasFixedSize(true);
        builder = new AlertDialog.Builder(getContext());
        efa=v.findViewById(R.id.extended_fab);
        efa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddNotice.class));
            }
        });

        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Notices");
        return v;
    }

    @Override
    public void onStart() {

        FirebaseRecyclerOptions<Notice> options=new FirebaseRecyclerOptions.Builder<Notice>().setQuery(dbref,Notice.class).build();

        FirebaseRecyclerAdapter<Notice,NoticeAdapter> adapter=new FirebaseRecyclerAdapter<Notice, NoticeAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoticeAdapter noticeAdapter, int i, @NonNull Notice notice) {

                noticeAdapter.title.setText(notice.getTitle());
                noticeAdapter.date.setText(notice.getData());
                noticeAdapter.time.setText(notice.getTime());
                Picasso.get().load(notice.getImage()).into(noticeAdapter.img);
                noticeAdapter.delete_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setMessage("Do you really want to delete the Notice!").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbref.child(notice.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Notice Deleted", Toast.LENGTH_SHORT).show();

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
                        alert.setTitle("Delete Notice");
                        alert.show();

                    }
                });
                noticeAdapter.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii=new Intent(getContext(), FullImage.class);
                        ii.putExtra("img",notice.getImage());
                        ii.putExtra("title",notice.getTitle());
                        startActivity(ii);
                    }
                });
            }

            @NonNull
            @Override
            public NoticeAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item_layout,parent,false);
               NoticeAdapter holder=new NoticeAdapter(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}