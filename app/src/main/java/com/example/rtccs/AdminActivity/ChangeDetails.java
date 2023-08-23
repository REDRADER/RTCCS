package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.example.rtccs.StudentActivity.StudentChangeDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ChangeDetails extends AppCompatActivity {
    EditText mname,memail;
    Button mupdate,close;
    CircleImageView profile_img;
    DatabaseReference dbref;
    StorageReference stref;
    String downloadurl="";
    private Uri uri;
    String checker="false";
    private StorageTask storageTask;
    ProgressDialog progressDialog;
    String year;
    String phone;
    String name,email,dbname="Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);
        mname=findViewById(R.id.fullname);
        memail=findViewById(R.id.email);
        profile_img=findViewById(R.id.profile_img);
        close=findViewById(R.id.close);
        mupdate=findViewById(R.id.update_stud);
        Paper.init(this);
        year=Prevalent.currentonlineuser.getYear();
        phone=Prevalent.currentonlineuser.getPhone();
        stref= FirebaseStorage.getInstance().getReference().child("Admin_Profile_Picture");
        progressDialog=new ProgressDialog(this);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        displayinfo();
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="true";
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .start(ChangeDetails.this);
            }
        });

        mupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("true"))
                {
                    progressDialog.setTitle("Uploading Profile");
                    progressDialog.setMessage("Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    updateimage();
                }
                else
                {
                    progressDialog.setTitle("Uploading Profile");
                    progressDialog.setMessage("Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    updatedata();

                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            uri=result.getUri();
            profile_img.setImageURI(uri);
        }
        else
        {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void updateimage() {
        dbref=FirebaseDatabase.getInstance().getReference().child(dbname).child(phone);

        final StorageReference filepath;
        filepath=stref.child("ProfileImages").child(phone+"jpg");
        final UploadTask uploadTask=filepath.putFile(uri);
        uploadTask.addOnCompleteListener(ChangeDetails.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    String name=mname.getText().toString();
                                    String email=memail.getText().toString();
                                    HashMap hashMap=new HashMap();
                                    hashMap.put("fullname",name);
                                    hashMap.put("email",email);
                                    hashMap.put("image",downloadurl);
                                    dbref.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ChangeDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ChangeDetails.this, "Profile not  updated", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(ChangeDetails.this, "Something went wrong in uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void updatedata() {
        dbref=FirebaseDatabase.getInstance().getReference().child(dbname).child(phone);

        String name=mname.getText().toString();
        String email=memail.getText().toString();
        HashMap hashMap=new HashMap();
        hashMap.put("fullname",name);
        hashMap.put("email",email);
        dbref.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(ChangeDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ChangeDetails.this, "Profile not  updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
    private void displayinfo() {
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        mname.setText(name);
        memail.setText(email);

        dbref= FirebaseDatabase.getInstance().getReference().child(dbname).child(phone);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String img=snapshot.child("image").getValue().toString();
                        Picasso.get().load(img).into(profile_img);
                    }
                    else
                    {
                        Toast.makeText(ChangeDetails.this, "No image found", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}