package com.example.rtccs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rtccs.Model.Teachers;
import com.example.rtccs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name,email,subject,phone;
     public Button update;
      public ImageView imageView;

    public TeacherAdapter(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        email=itemView.findViewById(R.id.email);
        subject=itemView.findViewById(R.id.subject);
        phone=itemView.findViewById(R.id.phone);
        update=itemView.findViewById(R.id.update_teacher);
        imageView=itemView.findViewById(R.id.img_teacher);

    }



    @Override
    public void onClick(View v) {

    }

//    private List<Teachers> list;
//    private Context context;
//
//    public TeacherAdapter(List<Teachers> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(context).inflate(R.layout.teachers_item_layout,parent,false);
//
//        return new TeacherViewAdapter(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TeacherViewAdapter holder, int position) {
//    Teachers item=list.get(position);
//    holder.name.setText(item.getName());
//    holder.email.setText(item.getEmail());
//    holder.phone.setText(item.getPhone());
//    holder.subject.setText(item.getSubject());
//    Picasso.get().load(item.getImage()).into(holder.imageView);
//    holder.update.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(context, "update teacher", Toast.LENGTH_SHORT).show();
//        }
//    });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class TeacherViewAdapter extends RecyclerView.ViewHolder {
//
//       private TextView name,email,subject,phone;
//        private Button update;
//        private ImageView imageView;
//
//
//        public TeacherViewAdapter(@NonNull View itemView) {
//            super(itemView);
//            name=itemView.findViewById(R.id.name);
//            email=itemView.findViewById(R.id.email);
//            subject=itemView.findViewById(R.id.subject);
//            phone=itemView.findViewById(R.id.phone);
//            update=itemView.findViewById(R.id.update_teacher);
//            imageView=itemView.findViewById(R.id.img_teacher);
//        }
//    }

}
