package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentIndexActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatUrls extends AppCompatActivity {
    TextView mcurrenturl;
   EditText mchat_url;
    Button mupdate;
    DatabaseReference dbref;
    String uri,year,url;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_urls);
        year=getIntent().getStringExtra("year");
        progressDialog=new ProgressDialog(this);
        mcurrenturl=findViewById(R.id.current_url);
      mchat_url=findViewById(R.id.chat_url);

        mcurrenturl.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent yyt =new Intent("android.intent.action.VIEW",
                      Uri.parse(mcurrenturl.getText().toString()));
              startActivity(yyt);
          }
      });
        mupdate=findViewById(R.id.update_chat);





        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatechaturl();

            }
        });

        dbref = FirebaseDatabase.getInstance().getReference().child("Chat").child(year);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uri = snapshot.child("uri").getValue(String.class);
                if (uri!=null)
                {
                    mcurrenturl.setText(uri);
                }
                else
                {
                    Toast.makeText(ChatUrls.this, "chat url not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatUrls.this, "error loading chat ", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void updatechaturl() {
        url=mchat_url.getText().toString();
        if (url.isEmpty())
        {
            Toast.makeText(ChatUrls.this, "enter new Url", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Updating url");
            progressDialog.setMessage("Wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }


        dbref=FirebaseDatabase.getInstance().getReference().child("Chat").child(year);
        HashMap hashMap=new HashMap();
        hashMap.put("uri",url);
        dbref.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(ChatUrls.this, "Link updated", Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}