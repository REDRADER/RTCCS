package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rtccs.Model.Notice;
import com.example.rtccs.Model.Teachers;
import com.example.rtccs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFaculty extends AppCompatActivity {
    EditText mname,memail,mphone,msubject;
    Button madd,mimg;
    CircleImageView mpreview;
    Bitmap bitmap;
    private DatabaseReference dbref;
    private StorageReference stref;
    private final int REQ= 1;
    String downloadurl="";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);
        mname=findViewById(R.id.name);
        mphone=findViewById(R.id.phone);
        memail=findViewById(R.id.email);
        msubject=findViewById(R.id.subject);
        madd=findViewById(R.id.add_teacher);
        mpreview=findViewById(R.id.add_teach_img);
        dbref= FirebaseDatabase.getInstance().getReference();
        stref= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        mpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        madd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mname.getText().toString().isEmpty())
                {
                    mname.setError("name is required");
                }
                if (memail.getText().toString().isEmpty())
                {
                    memail.setError("email is required");
                }
                if (mphone.getText().toString().isEmpty())
                {
                    mphone.setError("phone is required");
                }
                if (msubject.getText().toString().isEmpty())
                {
                    mphone.setError("subject is required");
                }

                else if (bitmap==null)
                {
                    progressDialog.setTitle("Uploading Profile");
                    progressDialog.setMessage("Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                   uploaddata();
                }
                else
                {
                    progressDialog.setTitle("Uploading Profile");
                    progressDialog.setMessage("Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    uploadnotice();
                }
            }
        });


    }

    private void uploadnotice() {

        ByteArrayOutputStream bob=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bob);
        byte[] finalimage=bob.toByteArray();
        final StorageReference filepath;
        filepath=stref.child("Teachers").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(AddFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadurl=String.valueOf(uri);
                                    uploaddata();
                                }
                            });
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(AddFaculty.this, "Something went wrong in uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void uploaddata() {
        dbref=dbref.child("Teachers");
       String name=mname.getText().toString();
       String email=memail.getText().toString();
       String phone=mphone.getText().toString();
       String subject=msubject.getText().toString();

        final String uniquekey=dbref.push().getKey();
//        Calendar calendar=Calendar.getInstance();
//        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MM-yy");
//        String date=currentdate.format(calendar.getTime());
//
//        Calendar calendarfortime=Calendar.getInstance();
//        SimpleDateFormat currenttime=new SimpleDateFormat("hh-mm a");
//        String time=currenttime.format(calendarfortime.getTime());

        Teachers teachers=new Teachers(name,downloadurl,email,phone,subject,uniquekey);

        dbref.child(uniquekey).setValue(teachers).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(AddFaculty.this, "Profile uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OpenGallery() {
        Intent pickimg=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimg,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mpreview.setImageBitmap(bitmap);
        }
    }
}