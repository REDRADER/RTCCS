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

import com.example.rtccs.Adapter.NoticeAdapter;
import com.example.rtccs.CommonActivity.FullImage;
import com.example.rtccs.Model.Notice;
import com.example.rtccs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class StudentNoticeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_student_notice, container, false);
        recyclerView=v.findViewById(R.id.notice);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbref= FirebaseDatabase.getInstance().getReference().child("Notices");
        return v;
    }

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<Notice> options=new FirebaseRecyclerOptions.Builder<Notice>().setQuery(dbref,Notice.class).build();

        FirebaseRecyclerAdapter<Notice, NoticeAdapter> adapter=new FirebaseRecyclerAdapter<Notice, NoticeAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoticeAdapter noticeAdapter, int i, @NonNull Notice notice) {

                noticeAdapter.title.setText(notice.getTitle());
                noticeAdapter.date.setText(notice.getData());
                noticeAdapter.time.setText(notice.getTime());
                Picasso.get().load(notice.getImage()).into(noticeAdapter.img);

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
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.student_notice_item_layout,parent,false);
                NoticeAdapter holder=new NoticeAdapter(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}