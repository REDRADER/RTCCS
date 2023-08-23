package com.example.rtccs.AdminActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rtccs.Model.Teachers;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateFaculty extends AppCompatActivity {
    EditText mname,memail,mphone,msubject;
    Button mupdate,mdelete,mimg;
    CircleImageView mpreview;
    Bitmap bitmap;
    private DatabaseReference dbref;
    private StorageReference stref;
    private final int REQ= 1;
    AlertDialog.Builder builder;
    String downloadurl="";
    private ProgressDialog progressDialog;
    String name,email,phone,subject,img,uniquekey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        mname=findViewById(R.id.up_name);
        mphone=findViewById(R.id.up_phone);
        memail=findViewById(R.id.up_email);
        msubject=findViewById(R.id.up_subject);
        builder = new AlertDialog.Builder(UpdateFaculty.this);
        mupdate=findViewById(R.id.update_teacher);
        mdelete=findViewById(R.id.delete_teacher);
        mpreview=findViewById(R.id.add_teach_img);
        dbref= FirebaseDatabase.getInstance().getReference();
        stref= FirebaseStorage.getInstance().getReference();
        uniquekey=getIntent().getStringExtra("uniquekey");
        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        phone=getIntent().getStringExtra("phone");
        subject=getIntent().getStringExtra("subject");
        img=getIntent().getStringExtra("img");
        if (!img.equals("")) {
            Picasso.get().load(img).into(mpreview);
        }
        mname.setText(name);
        mphone.setText(email);
        memail.setText(phone);
        msubject.setText(subject);







        progressDialog=new ProgressDialog(this);
        mpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        mupdate.setOnClickListener(new View.OnClickListener() {
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
                    updatedata();
                }
                else
                {
                    progressDialog.setTitle("Uploading Profile");
                    progressDialog.setMessage("Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    updateimage();
                }
            }
        });
        mdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    builder.setMessage("Do you really want to delete the Profile!").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteprofile();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.setTitle("Delete Profile");
                    alert.show();

                }catch (Exception e)
                {
                    Toast.makeText(UpdateFaculty.this, "somthing went wrong ", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        finish();
    }

    private void deleteprofile() {
        dbref.child("Teachers").child(uniquekey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                finish();
                Toast.makeText(UpdateFaculty.this, "Profile deleted Succesfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override

            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateFaculty.this, "Profile deltetion error ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateimage() {

        ByteArrayOutputStream bob=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bob);
        byte[] finalimage=bob.toByteArray();
        final StorageReference filepath;
        filepath=stref.child("Teachers").child(finalimage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateFaculty.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    downloadurl=String.valueOf(uri); updatedata();
                                }
                            });
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateFaculty.this, "Something went wrong in uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void updatedata() {

        String name=mname.getText().toString();
        String email=memail.getText().toString();
        String phone=mphone.getText().toString();
        String subject=msubject.getText().toString();
        HashMap hashMap=new HashMap();
        hashMap.put("name",name);
        hashMap.put("email",email);
        hashMap.put("phone",phone);
        hashMap.put("subject",subject);
      dbref.child("Teachers").child(uniquekey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(UpdateFaculty.this, "Profile uploaded", Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateFaculty.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OpenGallery() {
        try {
            Intent pickimge=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickimge,REQ);
        }catch (Exception e)
        {
            Log.e("image", "OpenGallery: ",e );
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mpreview.setImageBitmap(bitmap);
        }
    }
}
