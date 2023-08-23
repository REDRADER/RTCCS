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
import com.example.rtccs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNotice extends AppCompatActivity {

    EditText mtitle;
    Button mupload,mimg;
    ImageView mpreview;
    Bitmap bitmap;
    private DatabaseReference dbref;
    private StorageReference stref;
    private final int REQ= 1;
    String downloadurl="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);
        mtitle=findViewById(R.id.title);
        mpreview=findViewById(R.id.previewnotice);
        mimg=findViewById(R.id.addnotice);
        mupload=findViewById(R.id.upload);
        dbref=FirebaseDatabase.getInstance().getReference();
        stref= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        mimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpreview.setVisibility(View.VISIBLE);
                mimg.setVisibility(View.GONE);
                OpenGallery();
            }
        });

        mupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtitle.getText().toString().isEmpty())
                {
                    mtitle.setError("Title is required");
                }
                else if (bitmap==null)
                {
                    Toast.makeText(AddNotice.this, "please select the notice", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    mimg.setVisibility(View.GONE);
//                    mpreview.setVisibility(View.VISIBLE);



                    uploadnotice();
                }
            }
        });
        mpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadnotice();
            }
        });

    }

    private void uploadnotice() {
        progressDialog.setTitle("Uploading Notice");
        progressDialog.setMessage("Wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ByteArrayOutputStream bob=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bob);
        byte[] finalimage=bob.toByteArray();
        final StorageReference filepath;
        filepath=stref.child("Notices").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(AddNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                Toast.makeText(AddNotice.this, "Something went wrong in uploading", Toast.LENGTH_SHORT).show();
            }
            }
        });

    }

    private void uploaddata() {
        dbref=dbref.child("Notices");
        String title=mtitle.getText().toString();

        final String uniquekey=dbref.push().getKey();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MM-yy");
        String date=currentdate.format(calendar.getTime());

        Calendar calendarfortime=Calendar.getInstance();
        SimpleDateFormat currenttime=new SimpleDateFormat("hh-mm a");
        String time=currenttime.format(calendarfortime.getTime());

        Notice notice=new Notice(title,downloadurl,date,time,uniquekey);

        dbref.child(uniquekey).setValue(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(AddNotice.this, "notice uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddNotice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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