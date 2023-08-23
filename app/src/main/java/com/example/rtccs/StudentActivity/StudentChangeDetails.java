package com.example.rtccs.StudentActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rtccs.AdminActivity.UpdateFaculty;
import com.example.rtccs.Prevalent.Prevalent;
import com.example.rtccs.R;
import com.google.android.gms.tasks.Continuation;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class StudentChangeDetails extends AppCompatActivity {
    EditText mname,memail;
    Button mupdate,close;
    CircleImageView profile_img;
    DatabaseReference dbref;
     StorageReference stref;
    private final int REQ= 1;
    String downloadurl="";
    private Uri uri;
    String checker="false";
    private StorageTask storageTask;
    ProgressDialog progressDialog;
    String year;
    String phone;
    String name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_change_details);
        mname=findViewById(R.id.fullname);
        memail=findViewById(R.id.email);
        profile_img=findViewById(R.id.profile_img);
        close=findViewById(R.id.close);
        mupdate=findViewById(R.id.update_stud);
        Paper.init(this);
        year=Paper.book().read(Prevalent.userYearkey);
        phone=Paper.book().read(Prevalent.userPhonekey);
        stref=FirebaseStorage.getInstance().getReference().child("Profile_Picture");
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
                        .start(StudentChangeDetails.this);
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
//
//    private void updatewithoutphoto() {
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Students").child(year);
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("fullname",mname.getText().toString());
//        hashMap.put("email",memail.getText().toString());
//        ref.updateChildren(hashMap);
//    }

//    private void updatewithphoto() {
//        if (TextUtils.isEmpty(mname.getText().toString()))
//        {
//            mname.setError("Required");
//        }
//        else  if (TextUtils.isEmpty(memail.getText().toString()))
//        {
//                    progressDialog.setTitle("Updating Profile");
//        progressDialog.setMessage("Wait....");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//            memail.setError("Required");
//        }
//        else if (checker.equals("true"))
//        {
//                    progressDialog.setTitle("Updating Profile");
//        progressDialog.setMessage("Wait....");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//            uploadimg();
//        }
//
//    }

//    private void uploadimg() {
//        progressDialog.setTitle("Updating Profile");
//        progressDialog.setMessage("Wait....");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        if (uri!=null)
//        {
//            final StorageReference fileref=stref.child(Prevalent.currentonlineuser.getPhone()+".jpg");
//            storageTask=fileref.putFile(uri);
//            storageTask.continueWithTask(new Continuation() {
//                @Override
//                public Object then(@NonNull Task task) throws Exception {
//                   if (!task.isSuccessful())
//                   {
//                       throw task.getException();
//                   }
//
//                    return fileref.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful())
//                    {
//                        Uri downloadurl=task.getResult();
//                        myurl=downloadurl.toString();
//                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Students").child(Prevalent.currentonlineuser.getYear()).child(Prevalent.currentonlineuser.getPhone());
//                        HashMap<String,Object> hashMap=new HashMap<>();
//                        hashMap.put("fullname",mname.getText().toString());
//                        hashMap.put("email",memail.getText().toString());
//                        hashMap.put("image",downloadurl);
//                        ref.updateChildren(hashMap);
//                        progressDialog.dismiss();
//                        finish();
//
//                    }
//                }
//            });
//
//        }
//    }
    private void updateimage() {
        dbref=FirebaseDatabase.getInstance().getReference().child("Students").child(year).child(phone);

        final StorageReference filepath;
        filepath=stref.child("ProfileImages").child(phone+"jpg");
        final UploadTask uploadTask=filepath.putFile(uri);
        uploadTask.addOnCompleteListener(StudentChangeDetails.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                            Toast.makeText(StudentChangeDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(StudentChangeDetails.this, "Profile not  updated", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StudentChangeDetails.this, "Something went wrong in uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void updatedata() {
        dbref=FirebaseDatabase.getInstance().getReference().child("Students").child(year).child(phone);

        String name=mname.getText().toString();
        String email=memail.getText().toString();
        HashMap hashMap=new HashMap();
        hashMap.put("fullname",name);
        hashMap.put("email",email);
        dbref.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(StudentChangeDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(StudentChangeDetails.this, "Profile not  updated", Toast.LENGTH_SHORT).show();
                finish();
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

    private void displayinfo() {
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        mname.setText(name);
        memail.setText(email);

        dbref= FirebaseDatabase.getInstance().getReference().child("Students").child(year).child(phone);
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
                        Toast.makeText(StudentChangeDetails.this, "No image found", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}